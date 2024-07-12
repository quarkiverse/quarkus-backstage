package io.quarkiverse.backstage.deployment.visitors.api;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ApiSpecFluent;

public class ApplyApiOwner extends TypedVisitor<ApiSpecFluent<?>> {

    private final String owner;
    private final boolean force;

    public ApplyApiOwner(String owner) {
        this(owner, false);
    }

    public ApplyApiOwner(String owner, boolean force) {
        this.owner = owner;
        this.force = force;
    }

    @Override
    public void visit(ApiSpecFluent<?> spec) {
        if (force || !spec.hasOwner()) {
            spec.withOwner(owner);
        }
    }
}
