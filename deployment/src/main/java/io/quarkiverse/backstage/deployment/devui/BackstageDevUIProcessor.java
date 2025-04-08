package io.quarkiverse.backstage.deployment.devui;

import static io.quarkiverse.backstage.common.utils.Projects.getProjectInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import io.quarkiverse.argocd.spi.ArgoCDOutputDirBuildItem;
import io.quarkiverse.backstage.common.handlers.GetBackstageEntitiesAsStringHandler;
import io.quarkiverse.backstage.common.handlers.HandlerProcessor;
import io.quarkiverse.backstage.common.template.TemplateGenerator;
import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.utils.Strings;
import io.quarkiverse.backstage.deployment.BackstageConfiguration;
import io.quarkiverse.backstage.deployment.devservices.BackstageDevServiceInfoBuildItem;
import io.quarkiverse.backstage.runtime.devui.BackstageTemplateJsonRPCService;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.helm.spi.CustomHelmOutputDirBuildItem;
import io.quarkus.bootstrap.BootstrapException;
import io.quarkus.bootstrap.app.AugmentAction;
import io.quarkus.bootstrap.app.CuratedApplication;
import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.GeneratedFileSystemResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.dev.console.DevConsoleManager;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import io.quarkus.devui.spi.JsonRPCProvidersBuildItem;
import io.quarkus.devui.spi.buildtime.BuildTimeActionBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.jgit.deployment.GiteaDevServiceInfoBuildItem;
import io.quarkus.smallrye.openapi.deployment.spi.OpenApiDocumentBuildItem;

public class BackstageDevUIProcessor {

    private static final Logger LOG = Logger.getLogger(BackstageDevUIProcessor.class);

    @BuildStep(onlyIf = IsDevelopment.class)
    void createCard(
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BackstageConfiguration config,
            Optional<BackstageDevServiceInfoBuildItem> backstageServiceInfo,
            Optional<GiteaDevServiceInfoBuildItem> giteaServiceInfo,
            BuildProducer<CardPageBuildItem> cardPage) {
        backstageServiceInfo.ifPresent(i -> {
            String url = i.getUrl();
            CardPageBuildItem card = new CardPageBuildItem();
            card.addPage(Page.externalPageBuilder("Backstage")
                    .doNotEmbed()
                    .icon("font-awesome-solid:code-branch")
                    .url(url, url));
            cardPage.produce(card);

            String templateName = Optional.ofNullable(applicationInfo.getName())
                    .orElse(config.template().name().orElse("my-template"));
            String templateNamespace = config.template().namespace();
            Optional<Path> rootDir = Git.getRoot(outputTarget.getOutputDirectory());

            rootDir.ifPresent(r -> {
                card.addPage(Page.webComponentPageBuilder().title("Template Generator")
                        .componentLink("qwc-template.js")
                        .icon("font-awesome-solid:file-code"));

                card.getBuildTimeData().put("templateName", templateName);
                card.getBuildTimeData().put("templateNamespace", templateNamespace);
                card.getBuildTimeData().put("projectDir", r.toAbsolutePath().toString());
                card.getBuildTimeData().put("backstageUrl", url);
                card.getBuildTimeData().put("remoteName", config.git().remote());
                card.getBuildTimeData().put("remoteBranch", config.git().branch());
                card.getBuildTimeData().put("remoteUrl",
                        giteaServiceInfo
                                .map(g -> "http://" + g.host() + ":" + g.httpPort() + "/dev/" + applicationInfo.getName())
                                .orElse(null));
                giteaServiceInfo.ifPresent(info -> {
                    info.sharedNetworkHost().ifPresent(host -> {
                        int port = info.sharedNetworkHttpPort().orElse(3000);
                        card.getBuildTimeData().put("giteaSharedNetworkUrl",
                                "http://" + host + ":" + port + "/dev/" + applicationInfo.getName());
                        card.getBuildTimeData().put("giteaUsername", info.adminUsername());
                        card.getBuildTimeData().put("giteaPassword", info.adminPassword());
                    });
                });
            });
        });
    }

    @BuildStep
    void registerRpc(BuildProducer<JsonRPCProvidersBuildItem> jsonRPCProviders) {
        jsonRPCProviders.produce(new JsonRPCProvidersBuildItem(BackstageTemplateJsonRPCService.class));
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    BuildTimeActionBuildItem createBuildTimeActions(BackstageConfiguration config,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            Optional<OpenApiDocumentBuildItem> openApiBuildItem,
            Optional<ArgoCDOutputDirBuildItem.Effective> argoCDOutputDir,
            Optional<CustomHelmOutputDirBuildItem> helmOutputDir,
            Optional<GiteaDevServiceInfoBuildItem> giteaServiceInfo) {

        Optional<String> repositoryHost = giteaServiceInfo.map(i -> i.host() + ":" + i.httpPort());
        String remoteUrl = giteaServiceInfo
                .map(g -> "http://" + g.host() + ":" + g.httpPort() + "/dev/" + applicationInfo.getName()).orElse(null);
        String remoteName = config.git().remote();
        String remoteBracnh = config.git().branch();
        Optional<Path> rootDir = Git.getRoot(outputTarget.getOutputDirectory());
        Optional<Path> openApiSchemaDirectory = ConfigProvider.getConfig()
                .getOptionalValue("quarkus.smallrye-openapi.store-schema-directory", String.class).map(Paths::get);

        BuildTimeActionBuildItem generatorActions = new BuildTimeActionBuildItem();
        generatorActions.addAction("generateEntnties", ignored -> {
            try {
                LOG.debugf("Generating entities: %s" + ignored);
                List<Entity> entities = holder.generateEntities();
                Map<String, String> map = new LinkedHashMap<>();
                return map;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        generatorActions.addAction("generateTemplate", ignored -> {
            try {
                Optional<String> name = config.template().name().or(() -> Optional.ofNullable(applicationInfo.getName()));
                Optional<String> namespace = Optional.ofNullable(config.template().namespace());
                holder.generateTemplate(name, namespace, repositoryHost, openApiSchemaDirectory, openApiBuildItem,
                        argoCDOutputDir, helmOutputDir);
                return new HashMap<>();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return generatorActions;
    }

    static final Holder holder = new Holder();

    public static final class Holder {

        private void saveEntities(List<Entity> entities) {
            QuarkusBootstrap existing = (QuarkusBootstrap) DevConsoleManager.getQuarkusBootstrap();
            Path catalogInfoPath = existing.getProjectRoot().resolve("catalog-info.yaml");
            Strings.writeStringSafe(catalogInfoPath, Serialization.asYaml(entities));
        }

        private List<Entity> loadEntities() {
            QuarkusBootstrap existing = (QuarkusBootstrap) DevConsoleManager.getQuarkusBootstrap();
            Path catalogInfoPath = existing.getProjectRoot().resolve("catalog-info.yaml");
            return Serialization.unmarshalAsList(Strings.read(catalogInfoPath)).getItems();
        }

        public List<Entity> generateEntities() throws BootstrapException {
            QuarkusBootstrap existing = (QuarkusBootstrap) DevConsoleManager.getQuarkusBootstrap();
            synchronized (Holder.class) {
                QuarkusBootstrap quarkusBootstrap = existing.clonedBuilder()
                        .setMode(QuarkusBootstrap.Mode.PROD)
                        .setIsolateDeployment(false)
                        .setRebuild(true)
                        .setLocalProjectDiscovery(true)
                        .setBaseClassLoader(ClassLoader.getSystemClassLoader())
                        .build();

                try (CuratedApplication bootstrap = quarkusBootstrap.bootstrap()) {
                    AugmentAction augmentor = bootstrap.createAugmentor();
                    augmentor.performCustomBuild(GetBackstageEntitiesAsStringHandler.class.getName(),
                            new HandlerProcessor<String>() {
                                @Override
                                public void process(String catalogInfoContent, Path... paths) {
                                    Path catalogInfoPath = existing.getProjectRoot().resolve("catalog-info.yaml");
                                    Strings.writeStringSafe(catalogInfoPath, catalogInfoContent);
                                }
                            }, EntityListBuildItem.class.getName(), GeneratedFileSystemResourceBuildItem.class.getName());
                }
            }
            return loadEntities();
        }

        public Template generateTemplate(Optional<String> name, Optional<String> namespace,
                Optional<String> repositoryHost,
                Optional<Path> openApiSchemaDirectory,
                Optional<OpenApiDocumentBuildItem> openApiBuildItem,
                Optional<ArgoCDOutputDirBuildItem.Effective> argoCDOutputDir,
                Optional<CustomHelmOutputDirBuildItem> helmOutputDir) throws BootstrapException {

            List<Entity> entities = generateEntities();
            Map<String, String> parameters = new HashMap<>();
            QuarkusBootstrap existing = (QuarkusBootstrap) DevConsoleManager.getQuarkusBootstrap();
            QuarkusProject project = QuarkusProjectHelper.getProject(existing.getProjectRoot());
            parameters.putAll(getProjectInfo(project));

            LOG.debugf("Generating template from: %s", existing.getProjectRoot().toAbsolutePath());
            String templateName = name.orElse(parameters.getOrDefault("artifactId", "my-template"));
            parameters.put("componentId", templateName);
            List<Path> additionalFiles = new ArrayList<>();
            TemplateGenerator generator = new TemplateGenerator(project.getProjectDirPath(), templateName,
                    namespace.orElse("default"));

            repositoryHost.ifPresent(host -> generator.withRepositoryHost(host));

            openApiBuildItem.ifPresent(o -> {
                openApiSchemaDirectory.ifPresent(schemaDirectory -> {
                    additionalFiles.add(
                            project.getProjectDirPath().resolve(schemaDirectory).resolve("openapi.yaml"));
                });

            });
            argoCDOutputDir.ifPresent(a -> {
                generator.withArgoDirectory(a.getOutputDir());
            });
            helmOutputDir.ifPresent(h -> {
                generator.withHelmDirectory(h.getOutputDir());
            });

            generator.withAdditionalFiles(additionalFiles);
            Map<Path, String> templateContent = generator.generate();

            templateContent.forEach((path, content) -> {
                try {
                    Files.createDirectories(path.getParent());
                    Files.writeString(path, content);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to write file: " + path, e);
                }
            });

            Path backstageDir = project.getProjectDirPath().resolve(".backstage");
            Path templatesDir = backstageDir.resolve("templates");
            Path templateDir = templatesDir.resolve(templateName);

            Path templateYamlPath = templateDir.resolve("template.yaml");
            Template template = Serialization.unmarshal(templateContent.get(templateYamlPath), Template.class);
            return template;
        }
    }
}
