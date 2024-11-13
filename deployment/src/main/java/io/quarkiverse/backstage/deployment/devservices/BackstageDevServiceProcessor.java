package io.quarkiverse.backstage.deployment.devservices;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.testcontainers.shaded.com.google.common.io.Files;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.model.CreateLocationResponse;
import io.quarkiverse.backstage.common.dsl.Gitea;
import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.utils.Strings;
import io.quarkiverse.backstage.deployment.BackstageConfiguration;
import io.quarkiverse.backstage.deployment.BackstageProcessor;
import io.quarkiverse.backstage.spi.CatalogInstallationBuildItem;
import io.quarkiverse.backstage.spi.DevTemplateBuildItem;
import io.quarkiverse.backstage.spi.DevTemplateInstallationBuildItem;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.spi.TemplateBuildItem;
import io.quarkiverse.backstage.spi.TemplateInstallationBuildItem;
import io.quarkiverse.backstage.spi.UserProvidedTemplateBuildItem;
import io.quarkiverse.backstage.spi.UserProvidedTemplateInstallationBuildItem;
import io.quarkiverse.backstage.v1alpha1.Location;
import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.CuratedApplicationShutdownBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.devservices.common.ContainerShutdownCloseable;
import io.quarkus.jgit.deployment.GiteaDevServiceInfoBuildItem;
import io.quarkus.jgit.deployment.GiteaDevServiceRequestBuildItem;

public class BackstageDevServiceProcessor {

    private static final Logger log = Logger.getLogger(BackstageDevServiceProcessor.class);
    private static final String FALLBACK_USERNAME = "quarkus";
    private static final String FALLBACK_PASSWORD = "quarkus";

    @BuildStep(onlyIfNot = IsNormal.class, onlyIf = { GlobalDevServicesConfig.Enabled.class })
    void requestGitea(BackstageDevServicesConfig config, ApplicationInfoBuildItem applicationInfo,
            BuildProducer<GiteaDevServiceRequestBuildItem> giteaDevServiceRequest) {
        if (config.enabled()) {
            giteaDevServiceRequest.produce(
                    new GiteaDevServiceRequestBuildItem("gitea", List.of("dev"), List.of("dev/" + applicationInfo.getName())));
        }
    }

    @SuppressWarnings("resource")
    @BuildStep(onlyIfNot = IsNormal.class, onlyIf = { GlobalDevServicesConfig.Enabled.class })
    DevServicesResultBuildItem createContainer(
            BackstageConfiguration config,
            BackstageDevServicesConfig devServiceConfig,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            CuratedApplicationShutdownBuildItem closeBuildItem,
            EntityListBuildItem entityList,
            List<TemplateBuildItem> templates,
            List<UserProvidedTemplateBuildItem> userProvidedTemplates,
            List<DevTemplateBuildItem> devTemplates,
            Optional<GiteaDevServiceInfoBuildItem> giteaServiceInfo,
            BuildProducer<BackstageDevServiceInfoBuildItem> backstageDevServiceInfo,
            BuildProducer<CatalogInstallationBuildItem> catalogInstallation,
            BuildProducer<TemplateInstallationBuildItem> templateInstallation,
            BuildProducer<UserProvidedTemplateInstallationBuildItem> userProvidedTemplateInstallation,
            BuildProducer<DevTemplateInstallationBuildItem> devTemplateInstallation) throws IOException {

        var backstageServer = new BackstageContainer(devServiceConfig, giteaServiceInfo);
        backstageServer.start();
        String httpUrl = backstageServer.getHttpUrl();
        String token = devServiceConfig.token();
        log.infof("Backstage HTTP URL: %s", httpUrl);
        Map<String, String> configOverrides = new HashMap<>();
        configOverrides.put("quarkus.backstage.url", httpUrl);
        configOverrides.put("quarkus.backstage.token", token);
        giteaServiceInfo.ifPresent(gitea -> {
            configOverrides.put("quarkus.backstage.git.url", "http://" + gitea.host() + ":" + gitea.httpPort() + "/");
            configOverrides.put("quarkus.backstage.git.username", gitea.adminUsername());
            configOverrides.put("quarkus.backstage.git.password", gitea.adminPassword());
        });

        BackstageDevServiceInfoBuildItem info = new BackstageDevServiceInfoBuildItem(httpUrl, token);

        installCatalogInfo(config, devServiceConfig, applicationInfo, outputTarget, info, giteaServiceInfo, entityList,
                catalogInstallation);
        installTemplate(config, devServiceConfig, applicationInfo, outputTarget, info, giteaServiceInfo, templates,
                templateInstallation);
        installUserProvidedTemplate(config, devServiceConfig, applicationInfo, outputTarget, info, giteaServiceInfo,
                userProvidedTemplates, userProvidedTemplateInstallation);
        installDevTemplate(config, devServiceConfig, applicationInfo, outputTarget, info, giteaServiceInfo, devTemplates,
                devTemplateInstallation);

        backstageDevServiceInfo.produce(info);

        Path projectDirPath = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        Path backstageDevPath = projectDirPath.resolve(".quarkus").resolve("dev").resolve("backstage");
        Path connectionInfoPath = backstageDevPath.resolve(backstageServer.getContainerId() + ".yaml");
        Files.createParentDirs(connectionInfoPath.toFile());
        Strings.writeStringSafe(connectionInfoPath, Serialization.asYaml(info));

        ContainerShutdownCloseable closeable = new ContainerShutdownCloseable(backstageServer, BackstageProcessor.FEATURE);
        closeBuildItem.addCloseTask(closeable::close, true);
        closeBuildItem.addCloseTask(() -> {
            log.debug("Removing Backstage Dev Service connection info from .quarkus/dev/backstage");
            connectionInfoPath.toFile().delete();
        }, true);
        return new DevServicesResultBuildItem.RunningDevService("backstage", backstageServer.getContainerId(),
                backstageServer::close, configOverrides).toBuildItem();
    }

    void installCatalogInfo(BackstageConfiguration config,
            BackstageDevServicesConfig devServicesConfig,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BackstageDevServiceInfoBuildItem backstageDevServiceInfo,
            Optional<GiteaDevServiceInfoBuildItem> giteaDevServiceInfo,
            EntityListBuildItem entityList,
            BuildProducer<CatalogInstallationBuildItem> catalogInstallation) {

        if (!devServicesConfig.catalog().installation().enabled()) {
            return;
        }

        if (!giteaDevServiceInfo.isPresent()) {
            log.warn("Gitea Dev Service info not available, skipping catalog installation");
            return;
        }

        Path projectDirPath = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        String projectName = applicationInfo.getName();
        String content = Serialization.asYaml(entityList.getEntityList());
        Path catalogPath = Paths.get("catalog-info.yaml");
        Strings.writeStringSafe(catalogPath, content);

        BackstageClient backstageClient = new BackstageClient(backstageDevServiceInfo.getUrl(),
                backstageDevServiceInfo.getToken());
        Gitea gitea = giteaDevServiceInfo.map(Gitea::create).get().withRepository(projectName);
        gitea.pushProject(projectDirPath);
        gitea.withSharedReference(catalogPath, targetUrl -> {
            log.infof("Installing catalog-info.yaml to Backstage Dev Service: %s", targetUrl);
            Optional<Location> existingLocation = backstageClient.entities().list().stream()
                    .filter(e -> e.getKind().equals("Location"))
                    .map(e -> (Location) e)
                    .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                            || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                    .findFirst();

            if (existingLocation.isPresent()) {
                Location l = existingLocation.get();
                backstageClient.entities().withKind("location").withName(l.getMetadata().getName()).inNamespace("default")
                        .refresh();
            } else {
                CreateLocationResponse response = backstageClient.locations().createFromUrl(targetUrl);
            }
            catalogInstallation.produce(new CatalogInstallationBuildItem(entityList.getEntityList(), targetUrl));
        });
    }

    void installTemplate(BackstageConfiguration config,
            BackstageDevServicesConfig devServicesConfig,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BackstageDevServiceInfoBuildItem backstageDevServiceInfo,
            Optional<GiteaDevServiceInfoBuildItem> giteaDevServiceInfo,
            List<TemplateBuildItem> templates,
            BuildProducer<TemplateInstallationBuildItem> templateInstallation) {

        if (!devServicesConfig.template().installation().enabled()) {
            return;
        }
        List<TemplateInstallationBuildItem> installationBuildItems = doInstallTemplate(config, devServicesConfig,
                applicationInfo, outputTarget, backstageDevServiceInfo, giteaDevServiceInfo, templates);
        installationBuildItems.forEach(item -> templateInstallation.produce(item));
    }

    void installUserProvidedTemplate(BackstageConfiguration config,
            BackstageDevServicesConfig devServicesConfig,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BackstageDevServiceInfoBuildItem backstageDevServiceInfo,
            Optional<GiteaDevServiceInfoBuildItem> giteaDevServiceInfo,
            List<UserProvidedTemplateBuildItem> userProvideTemplates,
            BuildProducer<UserProvidedTemplateInstallationBuildItem> userProvidedTemplateInstallation) {

        if (!devServicesConfig.userProvidedTemplates().installation().enabled()) {
            return;
        }

        List<TemplateBuildItem> templates = userProvideTemplates.stream()
                .map(UserProvidedTemplateBuildItem::toTemplateBuildItem).collect(Collectors.toList());
        List<TemplateInstallationBuildItem> installationBuildItems = doInstallTemplate(config, devServicesConfig,
                applicationInfo, outputTarget, backstageDevServiceInfo, giteaDevServiceInfo, templates);
        installationBuildItems.forEach(item -> userProvidedTemplateInstallation
                .produce(new UserProvidedTemplateInstallationBuildItem(item.getTemplate(), item.getContent(), item.getUrl())));
    }

    void installDevTemplate(BackstageConfiguration config,
            BackstageDevServicesConfig devServicesConfig,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BackstageDevServiceInfoBuildItem backstageDevServiceInfo,
            Optional<GiteaDevServiceInfoBuildItem> giteaDevServiceInfo,
            List<DevTemplateBuildItem> devTemplates,
            BuildProducer<DevTemplateInstallationBuildItem> devTemplateInstallation) {

        if (!devServicesConfig.devTemplate().installation().enabled()) {
            return;
        }

        List<TemplateBuildItem> templates = devTemplates.stream().map(DevTemplateBuildItem::toTemplateBuildItem)
                .collect(Collectors.toList());
        List<TemplateInstallationBuildItem> installationBuildItems = doInstallTemplate(config, devServicesConfig,
                applicationInfo, outputTarget, backstageDevServiceInfo, giteaDevServiceInfo, templates);
        installationBuildItems.forEach(item -> devTemplateInstallation
                .produce(new DevTemplateInstallationBuildItem(item.getTemplate(), item.getContent(), item.getUrl())));
    }

    List<TemplateInstallationBuildItem> doInstallTemplate(BackstageConfiguration config,
            BackstageDevServicesConfig devServicesConfig,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BackstageDevServiceInfoBuildItem backstageDevServiceInfo,
            Optional<GiteaDevServiceInfoBuildItem> giteaDevServiceInfo,
            List<TemplateBuildItem> templates) {

        List<TemplateInstallationBuildItem> result = new ArrayList<>();

        if (!giteaDevServiceInfo.isPresent()) {
            log.warn("Gitea Dev Service info not available, skipping catalog installation");
            return result;
        }

        Path projectDirPath = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        String projectName = applicationInfo.getName();

        BackstageClient backstageClient = new BackstageClient(backstageDevServiceInfo.getUrl(),
                backstageDevServiceInfo.getToken());
        Gitea gitea = giteaDevServiceInfo.map(Gitea::create).get().withRepository(projectName);

        for (TemplateBuildItem template : templates) {
            Map<Path, String> templateContent = template.getContent();
            templateContent.forEach((path, content) -> {
                try {
                    Files.createParentDirs(path.toFile());
                    Strings.writeStringSafe(path, content);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to write template content to file: " + path, e);
                }
            });

            String templateName = template.getTemplate().getMetadata().getName();
            Path templatePath = projectDirPath.resolve(".backstage").resolve("templates").resolve(templateName)
                    .resolve("template.yaml");
            Path relativeTemplatePath = projectDirPath.relativize(templatePath);

            gitea.pushProject(projectDirPath);
            String[] templateUrl = new String[1];

            gitea.withSharedReference(relativeTemplatePath, targetUrl -> {
                log.infof("Installing Template to Backstage Dev Service: %s", targetUrl);
                Optional<Location> existingLocation = backstageClient.entities().list().stream()
                        .filter(e -> e.getKind().equals("Location"))
                        .map(e -> (Location) e)
                        .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                                || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                        .findFirst();

                if (existingLocation.isPresent()) {
                    Location l = existingLocation.get();
                    backstageClient.entities().withKind("location").withName(l.getMetadata().getName()).inNamespace("default")
                            .refresh();
                } else {
                    CreateLocationResponse response = backstageClient.locations().createFromUrl(targetUrl);
                }
                templateUrl[0] = targetUrl;
            });
            Optional.ofNullable(templateUrl[0]).map(url -> new TemplateInstallationBuildItem(template, url))
                    .ifPresent(result::add);
        }
        return result;
    }
}
