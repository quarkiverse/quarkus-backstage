package io.quarkiverse.backstage.common.visitors.template;

import java.util.Optional;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.OutputFluent;

public class AddNewUrlToOutput extends TypedVisitor<OutputFluent<?>> {

    private final String title;
    private final String url;
    private final Optional<String> icon;

    public AddNewUrlToOutput(String title, String url) {
        this(title, url, Optional.empty());
    }

    public AddNewUrlToOutput(String title, String url, Optional<String> icon) {
        this.title = title;
        this.url = url;
        this.icon = icon;
    }

    @Override
    public void visit(OutputFluent<?> output) {
        output.addNewLink()
                .withTitle(title)
                .withUrl(url)
                .withIcon(icon)
                .endLink();
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
