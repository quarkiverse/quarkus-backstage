package io.quarkiverse.backstage.spi;

import java.nio.file.Path;
import java.util.Map;

import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkus.builder.item.SimpleBuildItem;

public final class DevTemplateInstallationBuildItem extends SimpleBuildItem {
    private final Template template;
    private final Map<Path, String> content;
    private final String url;

    public DevTemplateInstallationBuildItem(DevTemplateBuildItem devTemplate, String url) {
        this(devTemplate.getTemplate(), devTemplate.getContent(), url);
    }

    public DevTemplateInstallationBuildItem(Template template, Map<Path, String> content, String url) {
        this.template = template;
        this.content = content;
        this.url = url;
    }

    public Template getTemplate() {
        return template;
    }

    public Map<Path, String> getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}