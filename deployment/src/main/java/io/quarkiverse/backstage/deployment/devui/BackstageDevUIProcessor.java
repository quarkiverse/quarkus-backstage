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

public class BackstageDevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    void createCard(
            ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTarget,
            BackstageConfiguration config,
            Optional<BackstageDevServiceInfoBuildItem> info, BuildProducer<CardPageBuildItem> cardPage) {
        info.ifPresent(i -> {
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
                card.getBuildTimeData().put("remote", config.git().remote());
                card.getBuildTimeData().put("branch", config.git().branch());
            });
        });
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    void registerRpc(BuildProducer<JsonRPCProvidersBuildItem> jsonRPCProviders) {
        jsonRPCProviders.produce(new JsonRPCProvidersBuildItem(BackstageTemplateJsonRPCService.class));
    }

    /*
     * @BuildStep(onlyIf = IsDevelopment.class)
     * BuildTimeActionBuildItem createBuildTimeActions() {
     * BuildTimeActionBuildItem actions = new BuildTimeActionBuildItem();
     * actions.addAction("generate", f -> {
     * String path = f.get("path");
     * String name = f.get("name");
     * String namespace = f.get("namespace");
     * Path rootDir = Paths.get(path);
     * TemplateGenerator generator = new TemplateGenerator(rootDir, name, namespace);
     * Map<Path, String> templateContent = generator.generate();
     *
     * templateContent.forEach((p, c) -> {
     * try {
     * Files.createDirectories(p.getParent());
     * Files.writeString(p, c);
     * } catch (IOException e) {
     * throw new RuntimeException("Failed to write file: " + path, e);
     * }
     * });
     *
     * Path backstageDir = rootDir.resolve(".backstage");
     * Path templatesDir = backstageDir.resolve("templates");
     * Path templateDir = templatesDir.resolve(name);
     *
     * Path templateYamlPath = templateDir.resolve("template.yaml");
     * return null;
     * });
     *
     * return actions;
     * }
     */
}
