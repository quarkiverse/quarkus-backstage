package io.quarkiverse.backstage.spi;

import java.nio.file.Path;
import java.util.Map;

import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkus.builder.item.MultiBuildItem;

public final class UserProvidedTemplateInstallationBuildItem extends MultiBuildItem {
    private final Template template;
    private final Map<Path, String> content;
    private final String url;

    public UserProvidedTemplateInstallationBuildItem(TemplateBuildItem template, String url) {
        this(template.getTemplate(), template.getContent(), url);
    }

    public UserProvidedTemplateInstallationBuildItem(Template template, Map<Path, String> content, String url) {
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
