package io.quarkiverse.backstage.deployment.visitors.component;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentSpecFluent;

public class ApplyComponentOwner extends TypedVisitor<ComponentSpecFluent<?>> {

    private final String owner;
    private final boolean force;

    public ApplyComponentOwner(String owner) {
        this(owner, false);
    }

    public ApplyComponentOwner(String owner, boolean force) {
        this.owner = owner;
        this.force = force;
    }

    @Override
    public void visit(ComponentSpecFluent<?> spec) {
        if (force || !spec.hasOwner()) {
            spec.withOwner(owner);
        }
    }
}
