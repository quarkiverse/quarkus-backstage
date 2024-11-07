package io.quarkiverse.backstage.deployment;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import io.quarkiverse.argocd.spi.ArgoCDOutputDirBuildItem;
import io.quarkiverse.backstage.common.template.TemplateGenerator;
import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.visitors.ApplyLifecycle;
import io.quarkiverse.backstage.common.visitors.ApplyOwner;
import io.quarkiverse.backstage.common.visitors.api.ApplyApiDescription;
import io.quarkiverse.backstage.common.visitors.api.ApplyApiTitle;
import io.quarkiverse.backstage.common.visitors.api.ApplyOpenApiDefinitionPath;
import io.quarkiverse.backstage.common.visitors.component.AddComponentApis;
import io.quarkiverse.backstage.common.visitors.component.AddComponentDependencies;
import io.quarkiverse.backstage.common.visitors.component.ApplyComponentAnnotation;
import io.quarkiverse.backstage.common.visitors.component.ApplyComponentLabel;
import io.quarkiverse.backstage.common.visitors.component.ApplyComponentName;
import io.quarkiverse.backstage.common.visitors.component.ApplyComponentTag;
import io.quarkiverse.backstage.common.visitors.component.ApplyComponentType;
import io.quarkiverse.backstage.model.builder.Visitor;
import io.quarkiverse.backstage.runtime.BackstageClientFactory;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.spi.DevTemplateBuildItem;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.spi.TemplateBuildItem;
import io.quarkiverse.backstage.v1alpha1.Api;
import io.quarkiverse.backstage.v1alpha1.ApiBuilder;
import io.quarkiverse.backstage.v1alpha1.Component;
import io.quarkiverse.backstage.v1alpha1.ComponentBuilder;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkiverse.backstage.v1alpha1.EntityListBuilder;
import io.quarkiverse.helm.spi.CustomHelmOutputDirBuildItem;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.builder.Version;
import io.quarkus.deployment.Feature;
import io.quarkus.deployment.IsTest;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.GeneratedFileSystemResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.jgit.deployment.GiteaDevServiceInfoBuildItem;
import io.quarkus.kubernetes.spi.CustomKubernetesOutputDirBuildItem;
import io.quarkus.smallrye.openapi.deployment.spi.OpenApiDocumentBuildItem;

class BackstageProcessor {

    private static final String FEATURE = "backstage";
    private static final String DOT_GIT = ".git";
    private static final Logger LOG = Logger.getLogger(BackstageProcessor.class);

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    public void registerBeanProducers(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemBuildItem) {
        additionalBeanBuildItemBuildItem.produce(AdditionalBeanBuildItem.unremovableOf(BackstageClientFactory.class));
    }

    @BuildStep
    public void configureKubernetesOutputDir(BuildProducer<CustomKubernetesOutputDirBuildItem> customKubernetesOutputDir) {
        customKubernetesOutputDir.produce(new CustomKubernetesOutputDirBuildItem(Paths.get(".kubernetes")));
    }

    @BuildStep
    public void configureHelmOutputDir(BuildProducer<CustomHelmOutputDirBuildItem> customHelmOutputDir) {
        customHelmOutputDir.produce(new CustomHelmOutputDirBuildItem(Paths.get(".helm")));
    }

    @BuildStep
    public void generateCatalogInfo(BackstageConfiguration config,
            ApplicationInfoBuildItem applicationInfo,
            List<FeatureBuildItem> features,
            OutputTargetBuildItem outputTarget,
            Optional<OpenApiDocumentBuildItem> openApiBuildItem,
            BuildProducer<EntityListBuildItem> entityListProducer) {

        Path projectRootDir = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        Path catalogInfoPath = projectRootDir.resolve("catalog-info.yaml");
        List<Entity> existingEntities = catalogInfoPath.toFile().exists()
                ? parseCatalogInfo(catalogInfoPath)
                : List.of();

        Optional<Component> existingComponent = findComponent(existingEntities);
        List<Entity> generatedEntities = new ArrayList<>();

        List<Visitor> visitors = new ArrayList<>();

        visitors.add(new ApplyLifecycle("production"));
        visitors.add(new ApplyOwner("user:guest"));

        boolean hasApi = openApiBuildItem.isPresent() && isOpenApiGenerationEnabled();
        if (hasApi) {
            generatedEntities.add(createOpenApiEntity(applicationInfo, openApiBuildItem.get(), visitors));
        }

        Component updatedComponent = createComponent(config, applicationInfo, projectRootDir, hasRestClient(features), hasApi,
                existingComponent, visitors);
        generatedEntities.add(updatedComponent);

        // Add all existing entities that are not already in the generated entities
        for (Entity entity : existingEntities) {
            if (containsEntity(generatedEntities, entity)) {
                continue;
            }
            generatedEntities.add(entity);
        }

        EntityList entityList = new EntityListBuilder()
                .withItems(generatedEntities)
                .build();

        entityListProducer.produce(new EntityListBuildItem(entityList));
    }

    @BuildStep(onlyIfNot = IsTest.class)
    public void saveCatalogInfo(BackstageConfiguration config,
            EntityListBuildItem entityList,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BuildProducer<GeneratedFileSystemResourceBuildItem> generatedResourceProducer) {

        if (!config.catalog().generation().enabled()) {
            return;
        }

        Path projectRootDir = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        Path catalogInfoPath = projectRootDir.resolve("catalog-info.yaml");
        var str = Serialization.asYaml(entityList.getEntityList());
        generatedResourceProducer.produce(new GeneratedFileSystemResourceBuildItem(catalogInfoPath.toAbsolutePath().toString(),
                str.getBytes(StandardCharsets.UTF_8)));
    }

    @BuildStep
    public void generateTemplate(BackstageConfiguration config, ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            Optional<OpenApiDocumentBuildItem> openApiBuildItem,
            Optional<ArgoCDOutputDirBuildItem.Effective> argoCDOutputDir,
            Optional<CustomHelmOutputDirBuildItem> helmOutputDir,
            EntityListBuildItem entityList,
            BuildProducer<TemplateBuildItem> templateProducer) {

        Path projectRootDir = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        String templateName = config.template().name().orElse(applicationInfo.getName());

        boolean hasApi = openApiBuildItem.isPresent() && isOpenApiGenerationEnabled();
        List<Path> additionalFiles = new ArrayList<>();
        if (hasApi) {
            ConfigProvider.getConfig().getOptionalValue("quarkus.smallrye-openapi.store-schema-directory", String.class)
                    .ifPresent(schemaDirectory -> {
                        additionalFiles.add(projectRootDir.resolve(Paths.get(schemaDirectory)).resolve("openapi.yaml"));
                    });
        }
        TemplateGenerator generator = new TemplateGenerator(projectRootDir, templateName, config.template().namespace())
                .withAdditionalFiles(additionalFiles)
                .withEntityList(entityList.getEntityList());

        argoCDOutputDir.ifPresent(a -> {
            generator.withArgoDirectory(a.getOutputDir());
        });

        helmOutputDir.ifPresent(h -> {
            generator.withHelmDirectory(h.getOutputDir());
        });

        Map<Path, String> templateContent = generator.generate();

        Path backstageDir = projectRootDir.resolve(".backstage");
        Path templatesDir = backstageDir.resolve("templates");
        Path templateDir = templatesDir.resolve(templateName);

        Path templateYamlPath = templateDir.resolve("template.yaml");
        Template template = Serialization.unmarshal(templateContent.get(templateYamlPath), Template.class);
        templateProducer.produce(new TemplateBuildItem(template, templateContent));
    }

    @BuildStep
    public void generateDevTemplate(BackstageConfiguration config,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            Optional<OpenApiDocumentBuildItem> openApiBuildItem,
            Optional<ArgoCDOutputDirBuildItem.Effective> argoCDOutputDir,
            Optional<CustomHelmOutputDirBuildItem> helmOutputDir,
            Optional<GiteaDevServiceInfoBuildItem> giteaDevServiceInfo,
            EntityListBuildItem entityList,
            BuildProducer<DevTemplateBuildItem> templateProducer) {

        Path projectRootDir = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        String templateName = config.devTemplate().name().orElse(applicationInfo.getName());
        String devTemplateName = config.devTemplate().name().orElse(applicationInfo.getName()) + "-dev";

        boolean hasApi = openApiBuildItem.isPresent() && isOpenApiGenerationEnabled();
        List<Path> additionalFiles = new ArrayList<>();
        if (hasApi) {
            ConfigProvider.getConfig().getOptionalValue("quarkus.smallrye-openapi.store-schema-directory", String.class)
                    .ifPresent(schemaDirectory -> {
                        additionalFiles.add(projectRootDir.resolve(Paths.get(schemaDirectory)).resolve("openapi.yaml"));
                    });
        }

        TemplateGenerator generator = new TemplateGenerator(projectRootDir, templateName, config.devTemplate().namespace())
                .withAdditionalFiles(additionalFiles)
                .withEntityList(entityList.getEntityList());

        giteaDevServiceInfo.ifPresent(info -> {
            generator.withRepositoryHost(
                    info.sharedNetworkHost().orElse("gitea") + ":" + info.sharedNetworkHttpPort().orElse(3000));
        });

        argoCDOutputDir.ifPresent(a -> {
            generator.withArgoDirectory(a.getOutputDir());
        });

        helmOutputDir.ifPresent(h -> {
            generator.withHelmDirectory(h.getOutputDir());
        });

        Map<Path, String> templateContent = generator.generate(true);
        Path backstageDir = projectRootDir.resolve(".backstage");
        Path templatesDir = backstageDir.resolve("templates");
        Path devTemplateDir = templatesDir.resolve(devTemplateName);

        Path devTemplateYamlPath = devTemplateDir.resolve("template.yaml");
        Template template = Serialization.unmarshal(templateContent.get(devTemplateYamlPath), Template.class);
        templateProducer.produce(new DevTemplateBuildItem(template, templateContent));
    }

    @BuildStep(onlyIf = IsTemplateGenerationEnabled.class)
    public void saveTemplate(BackstageConfiguration configuration, TemplateBuildItem template,
            BuildProducer<GeneratedFileSystemResourceBuildItem> generatedResourceProducer) {
        Map<Path, String> templateContent = template.getContent();
        templateContent.forEach((path, content) -> {
            generatedResourceProducer.produce(new GeneratedFileSystemResourceBuildItem(path.toAbsolutePath().toString(),
                    content.getBytes(StandardCharsets.UTF_8)));
        });
    }

    @BuildStep(onlyIf = IsDevTemplateGenerationEnabled.class)
    public void saveDevTemplate(BackstageConfiguration configuration, DevTemplateBuildItem devTemplate,
            BuildProducer<GeneratedFileSystemResourceBuildItem> generatedResourceProducer) {
        Map<Path, String> templateContent = devTemplate.getContent();
        templateContent.forEach((path, content) -> {
            generatedResourceProducer.produce(new GeneratedFileSystemResourceBuildItem(path.toAbsolutePath().toString(),
                    content.getBytes(StandardCharsets.UTF_8)));
        });
    }

    private static boolean hasRestClient(List<FeatureBuildItem> features) {
        return features.stream().anyMatch(f -> f.getName().equals(Feature.REST_CLIENT.getName()));
    }

    private Component createComponent(BackstageConfiguration config, ApplicationInfoBuildItem applicationInfo,
            Path projectRootDir, boolean hasRestClient,
            boolean hasApi,
            Optional<Component> existingComponent, List<Visitor> visitors) {

        Optional<String> gitRemoteUrl = Git.getRemoteUrl(projectRootDir, config.git().remote(), true);

        visitors.add(new ApplyComponentName(applicationInfo.getName()));
        visitors.add(new ApplyComponentLabel("app.kubernetes.io/name", applicationInfo.getName()));
        visitors.add(new ApplyComponentLabel("app.kubernetes.io/version", applicationInfo.getVersion()));
        visitors.add(new ApplyComponentLabel("app.quarkus.io/version", Version.getVersion()));
        visitors.add(new ApplyComponentAnnotation("backstage.io/source-location",
                gitRemoteUrl.map(u -> "url:" + u.replaceAll(".git$", ""))));
        visitors.add(new ApplyComponentTag("java"));
        visitors.add(new ApplyComponentTag("quarkus"));
        visitors.add(new ApplyComponentType("application"));

        if (hasRestClient) {
            List<String> restClientNames = StreamSupport
                    .stream(ConfigProvider.getConfig().getPropertyNames().spliterator(), false)
                    .map(Utils::getRestClientName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            visitors.add(new AddComponentDependencies(restClientNames));
        }

        if (hasApi) {
            visitors.add(new AddComponentApis(applicationInfo.getName() + "-api"));
        }

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
            OpenApiDocumentBuildItem openApiDocumentBuildItem,
            List<Visitor> visitors) {
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
                .withName(applicationInfo.getName() + "-api")
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
            List<String> lines = Files.readAllLines(path);
            //The list of lines contains multiple yamls separated by --
            //create a list of string that contains each yaml
            List<String> yamls = new ArrayList<>();
            StringBuilder yb = new StringBuilder();
            for (String line : lines) {
                if (line.equals("--")) {
                    yamls.add(yb.toString());
                    yb = new StringBuilder();
                } else {
                    yb.append(line).append("\n");
                }
            }
            List<Entity> entities = new ArrayList<>();
            for (String yaml : yamls) {
                try (InputStream is = new FileInputStream(yaml)) {
                    entities.add(Serialization.unmarshal(is));
                }
            }
            return entities;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse catalog info: " + path.toAbsolutePath(), e);
        }
    }
}
