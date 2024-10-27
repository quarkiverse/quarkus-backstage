package io.quarkiverse.backstage.deployment.devservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.Logger;

import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;
import io.quarkus.jgit.deployment.GiteaDevServiceInfoBuildItem;
import io.quarkus.jgit.deployment.GiteaDevServiceRequestBuildItem;

public class BackstageDevServiceProcessor {

    private static final Logger log = Logger.getLogger(BackstageDevServiceProcessor.class);

    @SuppressWarnings("resource")
    @BuildStep(onlyIfNot = IsNormal.class, onlyIf = { GlobalDevServicesConfig.Enabled.class })
    DevServicesResultBuildItem createContainer(BackstageDevServicesConfig config,
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

        giteaServiceInfo.ifPresent(giteaInfo -> {
            configOverrides.put("quarkus.backstage.git.url", "https://" + giteaInfo.host());
        });
        backstageDevServiceInfo.produce(new BackstageDevServiceInfoBuildItem(httpUrl, token));
        return new DevServicesResultBuildItem.RunningDevService("backstage", backstageServer.getContainerId(),
                backstageServer::close, configOverrides).toBuildItem();
    }

    @BuildStep(onlyIfNot = IsNormal.class, onlyIf = { GlobalDevServicesConfig.Enabled.class })
    void requestGitea(BackstageDevServicesConfig config, ApplicationInfoBuildItem applicationInfo,
            BuildProducer<GiteaDevServiceRequestBuildItem> giteaDevServiceRequest) {
        if (config.enabled()) {
            giteaDevServiceRequest.produce(
                    new GiteaDevServiceRequestBuildItem("gitea", List.of("dev"), List.of("dev/" + applicationInfo.getName())));
        }
    }
}
