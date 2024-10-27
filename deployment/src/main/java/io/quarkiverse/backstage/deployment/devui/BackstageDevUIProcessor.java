package io.quarkiverse.backstage.deployment.devui;

import java.nio.file.Path;
import java.util.Optional;

import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.deployment.BackstageConfiguration;
import io.quarkiverse.backstage.deployment.devservices.BackstageDevServiceInfoBuildItem;
import io.quarkiverse.backstage.runtime.devui.BackstageTemplateJsonRPCService;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.devui.spi.JsonRPCProvidersBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.jgit.deployment.GiteaDevServiceInfoBuildItem;

public class BackstageDevUIProcessor {

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
                    });
                });
            });
        });
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    void registerRpc(BuildProducer<JsonRPCProvidersBuildItem> jsonRPCProviders) {
        jsonRPCProviders.produce(new JsonRPCProvidersBuildItem(BackstageTemplateJsonRPCService.class));
    }
}
