package io.quarkiverse.backstage.deployment.visitors;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.EntityMetaFluent;

public class ApplyMetadataName extends TypedVisitor<EntityMetaFluent<?>> {

    private final String name;

    public ApplyMetadataName(String name) {
        this.name = name;
    }

    @Override
    public void visit(EntityMetaFluent<?> meta) {
        meta.withName(name);
    }
}
