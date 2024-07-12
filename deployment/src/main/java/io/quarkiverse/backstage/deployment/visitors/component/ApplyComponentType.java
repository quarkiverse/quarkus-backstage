package io.quarkiverse.backstage.deployment.visitors.component;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentSpecFluent;

public class ApplyComponentType extends TypedVisitor<ComponentSpecFluent<?>> {

    private final String type;
    private final boolean force;

    public ApplyComponentType(String type) {
        this(type, false);
    }

    public ApplyComponentType(String type, boolean force) {
        this.type = type;
        this.force = force;
    }

    @Override
    public void visit(ComponentSpecFluent<?> spec) {
        if (force || !spec.hasType()) {
            spec.withType(type);
        }
    }
}
