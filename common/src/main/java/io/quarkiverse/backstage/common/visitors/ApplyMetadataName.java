package io.quarkiverse.backstage.common.visitors;

import io.quarkiverse.backstage.EntityMetaFluent;
import io.quarkiverse.backstage.model.builder.TypedVisitor;

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
