package io.quarkiverse.backstage.common.visitors;

import io.quarkiverse.backstage.EntityMetaFluent;
import io.quarkiverse.backstage.model.builder.TypedVisitor;

public class ApplyMetadataTitle extends TypedVisitor<EntityMetaFluent<?>> {

    private final String title;

    public ApplyMetadataTitle(String title) {
        this.title = title;
    }

    @Override
    public void visit(EntityMetaFluent<?> meta) {
        meta.withTitle(title);
    }
}
