package io.quarkiverse.backstage.deployment.visitors.template;

import java.util.Optional;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.OutputFluent;

public class AddNewEntityRefToOutput extends TypedVisitor<OutputFluent<?>> {

    private final String title;
    private final String entityRef;
    private final Optional<String> icon;

    public AddNewEntityRefToOutput(String title, String entityRef) {
        this(title, entityRef, Optional.empty());
    }

    public AddNewEntityRefToOutput(String title, String entityRef, Optional<String> icon) {
        this.title = title;
        this.entityRef = entityRef;
        this.icon = icon;
    }

    @Override
    public void visit(OutputFluent<?> output) {
        output.addNewLink()
                .withTitle(title)
                .withEntityRef(entityRef)
                .withIcon(icon)
                .endLink();
    }

}
