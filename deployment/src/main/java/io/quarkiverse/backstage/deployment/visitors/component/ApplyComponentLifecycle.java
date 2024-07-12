package io.quarkiverse.backstage.deployment.visitors.component;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentSpecFluent;

public class ApplyComponentLifecycle extends TypedVisitor<ComponentSpecFluent<?>> {

    private final String lifecycle;
    private final boolean force;

    public ApplyComponentLifecycle(String lifecycle) {
        this(lifecycle, false);
    }

    public ApplyComponentLifecycle(String lifecycle, boolean force) {
        this.lifecycle = lifecycle;
        this.force = force;
    }

    @Override
    public void visit(ComponentSpecFluent<?> spec) {
        if (force || !spec.hasLifecycle()) {
            spec.withLifecycle(lifecycle);
        }
    }
}
