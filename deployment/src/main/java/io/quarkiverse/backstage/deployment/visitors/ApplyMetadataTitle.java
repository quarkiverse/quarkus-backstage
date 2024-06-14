package io.quarkiverse.backstage.deployment.visitors;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.EntityMetaFluent;

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
