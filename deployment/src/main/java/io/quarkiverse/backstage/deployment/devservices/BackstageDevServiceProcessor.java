package io.quarkiverse.backstage.deployment.devservices;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.Logger;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.dsl.GitActions;
import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.utils.Strings;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.EntityList;
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
    DevServicesResultBuildItem createContainer(BackstageDevServicesConfig config,
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            EntityListBuildItem entityList,
            Optional<GiteaDevServiceInfoBuildItem> giteaServiceInfo,
            BuildProducer<BackstageDevServiceInfoBuildItem> backstageDevServiceInfo) {

        var backstageServer = new BackstageContainer(config, giteaServiceInfo);
        backstageServer.start();
        String httpUrl = backstageServer.getHttpUrl();
        String token = config.token();
        log.infof("Backstage HTTP URL: %s", httpUrl);
        Map<String, String> configOverrides = new HashMap<>();
        configOverrides.put("quarkus.backstage.url", httpUrl);
        configOverrides.put("quarkus.backstage.token", token);

        Optional<String> giteaSharedUrl = giteaServiceInfo.flatMap(GiteaDevServiceInfoBuildItem::sharedNetworkHost)
                .map(host -> "http://" +
                        host + ":" +
                        giteaServiceInfo.get().sharedNetworkHttpPort().orElse(3000) +
                        "/dev/" +
                        applicationInfo.getName());

        Optional<String> giteaUrl = giteaServiceInfo
                .map(info -> "http://" + info.host() + ":" + info.httpPort() + "/dev/" + applicationInfo.getName());
        giteaUrl.ifPresent(url -> configOverrides.put("quarkus.backstage.gitea.url", url));

        backstageDevServiceInfo.produce(new BackstageDevServiceInfoBuildItem(httpUrl, token));
        BackstageClient client = new BackstageClient(backstageServer.getHost(), backstageServer.getHttpPort(), token);
        Optional<Path> projectDirPath = Git.getScmRoot(outputTarget.getOutputDirectory());
        projectDirPath.ifPresentOrElse(
                path -> installCatalogInfo(client, path, entityList.getEntityList(), giteaUrl, giteaSharedUrl),
                () -> log.warn("Could not find project root directory to install catalog-info.yaml to Backstage Dev Service."));

        return new DevServicesResultBuildItem.RunningDevService("backstage", backstageServer.getContainerId(),
                backstageServer::close, configOverrides).toBuildItem();
    }

    void installCatalogInfo(BackstageClient backstageClient, Path projectDirPath, EntityList entityList,
            Optional<String> giteaUrl, Optional<String> giteaSharedUrl) {
        String content = Serialization.asYaml(entityList);

        Path catalogPath = Paths.get("catalog-info.yaml");
        Strings.writeStringSafe(catalogPath, content);

        giteaUrl.ifPresentOrElse(targetUrl -> {
            commitAndPush(projectDirPath, targetUrl, "origin", "backstage");
        }, () -> log.warn("Cannot install catalog-info.yaml to Backstage Dev Service. No Gitea URL found."));

        giteaSharedUrl.flatMap(u -> Git.getUrlFromBase(u, "backstage", catalogPath)).ifPresentOrElse(targetUrl -> {
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
                backstageClient.locations().createFromUrl(targetUrl);
            }

        }, () -> log.warn("Cannot install catalog-info.yaml to Backstage Dev Service. No Gitea shared URL found."));
    }

    private boolean commitAndPush(Path rootDir, String remoteUrl, String remoteName, String remoteBranch) {
        Path dotBackstage = rootDir.relativize(rootDir.resolve(".backstage"));
        Path catalogInfoYaml = rootDir.relativize(rootDir.resolve("catalog-info.yaml"));
        if (remoteUrl != null) {
            GitActions.createTempo()
                    .addRemote(remoteName, remoteUrl)
                    .createBranch(remoteBranch)
                    .importFiles(rootDir, dotBackstage, catalogInfoYaml)
                    .commit("Generated backstage resources.", dotBackstage, catalogInfoYaml)
                    .push(remoteName, remoteBranch, "quarkus", "quarkus");
            return true;
        }

        GitActions.createTempo()
                .checkoutOrCreateBranch(remoteName, remoteBranch)
                .importFiles(rootDir, dotBackstage, catalogInfoYaml)
                .commit("Generated backstage resources.", dotBackstage, catalogInfoYaml)
                .push(remoteName, remoteBranch);

        return true;
    }
}
