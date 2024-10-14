package io.quarkiverse.backstage.common.visitors;

import io.quarkiverse.backstage.EntityMetaFluent;
import io.quarkiverse.backstage.model.builder.TypedVisitor;

public class ApplyMetadataDescription extends TypedVisitor<EntityMetaFluent<?>> {

    private final String description;

    public ApplyMetadataDescription(String description) {
        this.description = description;
    }

    @Override
    public void visit(EntityMetaFluent<?> meta) {
        meta.withDescription(description);
    }
}
