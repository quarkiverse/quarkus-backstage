package io.quarkiverse.backstage.deployment.visitors.component;

import io.quarkiverse.backstage.deployment.visitors.ApplyMetadataTag;
import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentFluent;

public class ApplyComponentTag extends TypedVisitor<ComponentFluent<?>> {

    private final String tag;

    public ApplyComponentTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void visit(ComponentFluent<?> component) {
        component.accept(new ApplyMetadataTag(tag));
    }
}
