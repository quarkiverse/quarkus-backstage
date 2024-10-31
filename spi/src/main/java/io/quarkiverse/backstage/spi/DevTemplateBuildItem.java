package io.quarkiverse.backstage.spi;

import java.nio.file.Path;
import java.util.Map;

import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkus.builder.item.SimpleBuildItem;

public final class DevTemplateBuildItem extends SimpleBuildItem {
    private final Template template;
    private final Map<Path, String> content;

    public DevTemplateBuildItem(Template template, Map<Path, String> content) {
        this.template = template;
        this.content = content;
    }

    public Template getTemplate() {
        return template;
    }

    public Map<Path, String> getContent() {
        return content;
    }
}
