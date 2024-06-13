package io.quarkiverse.backstage.deployment.visitors;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.EntityMetaFluent;

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
