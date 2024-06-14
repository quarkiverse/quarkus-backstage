package io.quarkiverse.backstage.deployment;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.quarkiverse.backstage.deployment.utils.Git;
import io.quarkiverse.backstage.deployment.visitors.api.ApplyApiDescription;
import io.quarkiverse.backstage.deployment.visitors.api.ApplyApiTitle;
import io.quarkiverse.backstage.deployment.visitors.api.ApplyOpenApiDefinitionPath;
import io.quarkiverse.backstage.deployment.visitors.component.ApplyComponentLabel;
import io.quarkiverse.backstage.deployment.visitors.component.ApplyComponentName;
import io.quarkiverse.backstage.deployment.visitors.component.ApplyComponentTag;
import io.quarkiverse.backstage.model.builder.Visitor;
import io.quarkiverse.backstage.v1alpha1.Api;
import io.quarkiverse.backstage.v1alpha1.ApiBuilder;
import io.quarkiverse.backstage.v1alpha1.Component;
import io.quarkiverse.backstage.v1alpha1.ComponentBuilder;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkus.builder.Version;
import io.quarkus.deployment.Feature;
import io.quarkus.deployment.IsTest;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.GeneratedFileSystemResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.smallrye.openapi.deployment.spi.OpenApiDocumentBuildItem;

class BackstageProcessor {

    private static final String FEATURE = "backstage";
    private static final String DOT_GIT = ".git";

    private static YAMLFactory YAML_FACTORY = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
    private static final ObjectMapper YAML_MAPPER = new YAMLMapper(YAML_FACTORY).registerModule(new Jdk8Module())
            .setSerializationInclusion(Include.NON_EMPTY);
    private static final JavaType ENTITY_LIST = YAML_MAPPER.getTypeFactory().constructCollectionType(List.class, Entity.class);

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIfNot = IsTest.class)
    public void build(ApplicationInfoBuildItem applicationInfo,
            List<FeatureBuildItem> features,
            OutputTargetBuildItem outputTarget,
            Optional<OpenApiDocumentBuildItem> openApiBuildItem,
            BuildProducer<GeneratedFileSystemResourceBuildItem> generatedResourceProducer) {

        Optional<Path> scmRoot = getScmRoot(outputTarget);

        Path catalogInfoPath = scmRoot.map(p -> p.resolve("catalog-info.yaml")).orElse(Paths.get("catalog-info.yaml"));
        List<Entity> existingEntities = catalogInfoPath.toFile().exists()
                ? parseCatalogInfo(catalogInfoPath)
                : List.of();

        Optional<Component> existingComponent = findComponent(existingEntities);
        List<Entity> generatedEntities = new ArrayList<>();

        Component updatedComponent = createComponent(applicationInfo, scmRoot, existingComponent);
        generatedEntities.add(updatedComponent);

        if (openApiBuildItem.isPresent() && isOpenApiGenerationEnabled()) {
            generatedEntities.add(createOpenApiEntity(applicationInfo, openApiBuildItem.get()));
        }

        // Add all existing entities that are not already in the generated entities
        for (Entity entity : existingEntities) {
            if (containsEntity(generatedEntities, entity)) {
                continue;
            }
            generatedEntities.add(entity);
        }

        try {
            var str = YAML_MAPPER.writeValueAsString(generatedEntities);
            generatedResourceProducer.produce(
                    new GeneratedFileSystemResourceBuildItem(catalogInfoPath.toAbsolutePath().toString(),
                            str.getBytes(StandardCharsets.UTF_8)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean hasRestClient(List<Feature> features) {
        return features.stream().anyMatch(f -> f.getName().equals(Feature.REST_CLIENT.getName()));
    }

    private Component createComponent(ApplicationInfoBuildItem applicationInfo, Optional<Path> scmRoot,
            Optional<Component> existingComponent) {
        final List<Visitor> visitors = new ArrayList<>();
        Config config = ConfigProvider.getConfig();
        Optional<String> gitRemoteUrl = scmRoot.flatMap(p -> Git.getRemoteUrl(p, "origin"));

        visitors.add(new ApplyComponentName(applicationInfo.getName()));
        visitors.add(new ApplyComponentLabel("app.kubernetes.io/name", applicationInfo.getName()));
        visitors.add(new ApplyComponentLabel("app.kubernetes.io/version", applicationInfo.getVersion()));
        visitors.add(new ApplyComponentLabel("app.quarkus.io/version", Version.getVersion()));
        visitors.add(new ApplyComponentLabel("backstage.io/source-location", gitRemoteUrl.map(u -> "url:" + u)));
        visitors.add(new ApplyComponentTag("java"));
        visitors.add(new ApplyComponentTag("quarkus"));

        return existingComponent.map(ComponentBuilder::new)
                .orElseGet(ComponentBuilder::new)
                .accept(visitors.toArray(Visitor[]::new))
                .build();
    }

    private boolean isOpenApiGenerationEnabled() {
        return ConfigProvider.getConfig().getOptionalValue("quarkus.smallrye-openapi.store-schema-directory", String.class)
                .isPresent();
    }

    private Api createOpenApiEntity(ApplicationInfoBuildItem applicationInfo,
            OpenApiDocumentBuildItem openApiDocumentBuildItem) {
        final List<Visitor> visitors = new ArrayList<>();
        Config config = ConfigProvider.getConfig();

        config.getOptionalValue("quarkus.smallrye-openapi.store-schema-directory", String.class).ifPresent(schemaDirectory -> {
            visitors.add(new ApplyOpenApiDefinitionPath(Paths.get(schemaDirectory).resolve("openapi.yaml").toString()));
        });

        config.getOptionalValue("quarkus.smallrye-openapi.info-title", String.class).ifPresent(title -> {
            visitors.add(new ApplyApiTitle(title));
        });

        config.getOptionalValue("quarkus.smallrye-openapi.info-description", String.class).ifPresent(description -> {
            visitors.add(new ApplyApiDescription(description));
        });

        return new ApiBuilder()
                .withNewMetadata()
                .withName(applicationInfo.getName())
                .endMetadata()
                .withNewSpec()
                .withType("openapi")
                .endSpec()
                .accept(visitors.toArray(Visitor[]::new))
                .build();
    }

    private static boolean entitiesMatch(Entity a, Entity b) {
        return a.getMetadata().getName().equals(b.getMetadata().getName()) && a.getKind().equals(b.getKind());
    }

    private static boolean containsEntity(List<Entity> entities, Entity entity) {
        return entities.stream().anyMatch(e -> entitiesMatch(e, entity));
    }

    private static Optional<Component> findComponent(List<Entity> entities) {
        return entities.stream()
                .filter(Component.class::isInstance)
                .map(Component.class::cast)
                .findFirst();
    }

    /**
     * Parse the catalog-info.yaml file into a list of entities
     *
     * @param path the path to the catalog-info.yaml file
     * @return the list of entities
     */
    private static List<Entity> parseCatalogInfo(Path path) {
        try {
            return YAML_MAPPER.readValue(path.toFile(), ENTITY_LIST);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse catalog info: " + path.toAbsolutePath(), e);
        }
    }

    /**
     * @return the SCM root directory of the project
     */
    private static Optional<Path> getScmRoot(OutputTargetBuildItem outputTarget) {
        Path dir = outputTarget.getOutputDirectory();
        while (dir != null && !dir.resolve(DOT_GIT).toFile().exists()) {
            dir = dir.getParent();
        }
        return Optional.ofNullable(dir).filter(p -> p.resolve(DOT_GIT).toFile().exists());
    }
}
