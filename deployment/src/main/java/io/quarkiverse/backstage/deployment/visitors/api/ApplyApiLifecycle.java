package io.quarkiverse.backstage.deployment.visitors.api;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ApiSpecFluent;

public class ApplyApiLifecycle extends TypedVisitor<ApiSpecFluent<?>> {

    private final String lifecycle;
    private final boolean force;

    public ApplyApiLifecycle(String lifecycle) {
        this(lifecycle, false);
    }

    public ApplyApiLifecycle(String lifecycle, boolean force) {
        this.lifecycle = lifecycle;
        this.force = force;
    }

    @Override
    public void visit(ApiSpecFluent<?> spec) {
        if (force || !spec.hasLifecycle()) {
            spec.withLifecycle(lifecycle);
        }
    }
}
