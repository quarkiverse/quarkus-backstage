package io.quarkiverse.backstage.deployment;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.quarkiverse.backstage.deployment.visitors.ApplyComponentName;
import io.quarkiverse.backstage.v1alpha1.Component;
import io.quarkiverse.backstage.v1alpha1.ComponentBuilder;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkus.deployment.IsTest;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.GeneratedFileSystemResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;

class BackstageProcessor {

    private static final String FEATURE = "backstage";
    private static final String DOT_GIT = ".git";
    private static final ObjectMapper YAML_MAPPER = new YAMLMapper().registerModule(new Jdk8Module())
            .setSerializationInclusion(Include.NON_EMPTY);
    private static final JavaType ENTITY_LIST = YAML_MAPPER.getTypeFactory().constructCollectionType(List.class, Entity.class);

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIfNot = IsTest.class)
    public void build(ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTargetBuildItem,
            BuildProducer<GeneratedFileSystemResourceBuildItem> generatedResourceProducer) {

        var newComponent = new ComponentBuilder()
                .withNewMetadata()
                .withName(applicationInfo.getName())
                .endMetadata()
                .withNewSpec()
                .endSpec()
                .build();

        Optional<Path> scmRoot = getScmRoot(outputTargetBuildItem);
        Path catalogInfoPath = scmRoot.map(p -> p.resolve("catalog-info.yaml")).orElse(Paths.get("catalog-info.yaml"));

        List<Entity> existingEntities = catalogInfoPath.toFile().exists()
                ? parseCatalogInfo(catalogInfoPath)
                : List.of();

        Optional<Component> existingComponent = getComponent(existingEntities);

        List<Entity> updatedEntities = new ArrayList<>(
                existingEntities.stream().filter(e -> !Component.class.isInstance(e)).collect(Collectors.toList()));
        Component updatedComponent = new ComponentBuilder(existingComponent.orElse(newComponent))
                .accept(new ApplyComponentName(applicationInfo.getName()))
                .build();

        updatedEntities.add(updatedComponent);
        try {
            var str = YAML_MAPPER.writeValueAsString(updatedEntities);
            generatedResourceProducer.produce(
                    new GeneratedFileSystemResourceBuildItem(catalogInfoPath.toAbsolutePath().toString(),
                            str.getBytes(StandardCharsets.UTF_8)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<Component> getComponent(List<Entity> entities) {
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
    private static Optional<Path> getScmRoot(OutputTargetBuildItem outputTargetBuildItem) {
        Path dir = outputTargetBuildItem.getOutputDirectory();
        while (dir != null && !dir.resolve(DOT_GIT).toFile().exists()) {
            dir = dir.getParent();
        }
        return Optional.ofNullable(dir).filter(p -> p.resolve(DOT_GIT).toFile().exists());
    }
}
