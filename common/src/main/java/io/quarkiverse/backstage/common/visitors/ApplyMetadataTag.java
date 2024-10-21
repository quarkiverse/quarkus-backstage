package io.quarkiverse.backstage.common.visitors;

import io.quarkiverse.backstage.EntityMetaFluent;
import io.quarkiverse.backstage.model.builder.TypedVisitor;

public class ApplyMetadataTag extends TypedVisitor<EntityMetaFluent<?>> {

    private final String tag;

    public ApplyMetadataTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void visit(EntityMetaFluent<?> meta) {
        if (meta.hasTags() && meta.hasMatchingTag(t -> t.equals(tag))) {
            return;
        }
        meta.addToTags(tag);
    }
}
