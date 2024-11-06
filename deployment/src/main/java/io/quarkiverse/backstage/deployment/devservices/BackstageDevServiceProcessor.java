package io.quarkiverse.backstage.deployment.devservices;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.testcontainers.shaded.com.google.common.io.Files;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.model.CreateLocationResponse;
import io.quarkiverse.backstage.common.dsl.Gitea;
import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.utils.Strings;
import io.quarkiverse.backstage.deployment.BackstageConfiguration;
import io.quarkiverse.backstage.spi.CatalogInstallationBuildItem;
import io.quarkiverse.backstage.spi.DevTemplateBuildItem;
import io.quarkiverse.backstage.spi.DevTemplateInstallationBuildItem;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.spi.TemplateBuildItem;
import io.quarkiverse.backstage.spi.TemplateInstallationBuildItem;
import io.quarkiverse.backstage.v1alpha1.Location;
import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
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
            EntityListBuildItem entityList,
            TemplateBuildItem template,
            DevTemplateBuildItem devTemplate,
            Optional<GiteaDevServiceInfoBuildItem> giteaServiceInfo,
            BuildProducer<BackstageDevServiceInfoBuildItem> backstageDevServiceInfo,
            BuildProducer<CatalogInstallationBuildItem> catalogInstallation,
            BuildProducer<TemplateInstallationBuildItem> templateInstallation,
            BuildProducer<DevTemplateInstallationBuildItem> devTemplateInstallation) {

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

        installCatalogInfo(config, devServiceConfig, applicationInfo, outputTarget, info, giteaServiceInfo,
                entityList, catalogInstallation);
        installTemplate(config, devServiceConfig, applicationInfo, outputTarget, info, giteaServiceInfo, template,
                entityList, templateInstallation);
        installDevTemplate(config, devServiceConfig, applicationInfo, outputTarget, info, giteaServiceInfo,
                devTemplate, entityList, devTemplateInstallation);

        backstageDevServiceInfo.produce(info);
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
            TemplateBuildItem template,
            EntityListBuildItem entityList,
            BuildProducer<TemplateInstallationBuildItem> templateInstallation) {

        if (!devServicesConfig.template().installation().enabled()) {
            return;
        }

        if (!giteaDevServiceInfo.isPresent()) {
            log.warn("Gitea Dev Service info not available, skipping catalog installation");
            return;
        }

        Path projectDirPath = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        String projectName = applicationInfo.getName();

        Map<Path, String> templateContent = template.getContent();
        templateContent.forEach((path, content) -> {
            try {
                Files.createParentDirs(path.toFile());
                Strings.writeStringSafe(path, content);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write template content to file: " + path, e);
            }
        });

        String content = Serialization.asYaml(entityList.getEntityList());
        Path catalogPath = Paths.get("catalog-info.yaml");
        Strings.writeStringSafe(catalogPath, content);

        String templateName = config.template().name().orElse(applicationInfo.getName());
        Path templatePath = projectDirPath.resolve(".backstage").resolve("templates").resolve(applicationInfo.getName())
                .resolve("template.yaml");
        Path relativeTemplatePath = projectDirPath.relativize(templatePath);

        BackstageClient backstageClient = new BackstageClient(backstageDevServiceInfo.getUrl(),
                backstageDevServiceInfo.getToken());
        Gitea gitea = giteaDevServiceInfo.map(Gitea::create).get().withRepository(projectName);
        gitea.pushProject(projectDirPath);
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
            templateInstallation.produce(new TemplateInstallationBuildItem(template, targetUrl));
        });
    }

    void installDevTemplate(BackstageConfiguration config,
            BackstageDevServicesConfig devServicesConfig,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BackstageDevServiceInfoBuildItem backstageDevServiceInfo,
            Optional<GiteaDevServiceInfoBuildItem> giteaDevServiceInfo,
            DevTemplateBuildItem devTemplate,
            EntityListBuildItem entityList,
            BuildProducer<DevTemplateInstallationBuildItem> devTemplateInstallation) {

        if (!devServicesConfig.devTemplate().installation().enabled()) {
            return;
        }

        if (!giteaDevServiceInfo.isPresent()) {
            log.warn("Gitea Dev Service info not available, skipping catalog installation");
            return;
        }

        Path projectDirPath = Projects.getProjectRoot(outputTarget.getOutputDirectory());
        String projectName = applicationInfo.getName();

        Map<Path, String> templateContent = devTemplate.getContent();
        templateContent.forEach((path, content) -> {
            try {
                Files.createParentDirs(path.toFile());
                Strings.writeStringSafe(path, content);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write template content to file: " + path, e);
            }
        });

        String content = Serialization.asYaml(entityList.getEntityList());
        Path catalogPath = Paths.get("catalog-info.yaml");
        Strings.writeStringSafe(catalogPath, content);

        String templateName = config.devTemplate().name().orElse(applicationInfo.getName() + "-dev");
        Path templatePath = projectDirPath.resolve(".backstage").resolve("templates").resolve(templateName)
                .resolve("template.yaml");
        Path relativeTemplatePath = projectDirPath.relativize(templatePath);

        BackstageClient backstageClient = new BackstageClient(backstageDevServiceInfo.getUrl(),
                backstageDevServiceInfo.getToken());
        Gitea gitea = giteaDevServiceInfo.map(Gitea::create).get().withRepository(projectName);
        gitea.pushProject(projectDirPath);
        gitea.withSharedReference(relativeTemplatePath, targetUrl -> {
            log.infof("Installing dev Template to Backstage Dev Service: %s", targetUrl);
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
            devTemplateInstallation.produce(new DevTemplateInstallationBuildItem(devTemplate, targetUrl));
        });
    }
}
