package io.quarkiverse.backstage.deployment.visitors;

import io.quarkiverse.backstage.deployment.visitors.api.ApplyApiLifecycle;
import io.quarkiverse.backstage.deployment.visitors.component.ApplyComponentLifecycle;
import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.TypedVisitor;

public class ApplyLifecycle extends TypedVisitor<BaseFluent<?>> {

    private final String lifecycle;
    private final boolean force;

    public ApplyLifecycle(String lifecycle) {
        this(lifecycle, false);
    }

    public ApplyLifecycle(String lifecycle, boolean force) {
        this.lifecycle = lifecycle;
        this.force = force;
    }

    @Override
    public void visit(BaseFluent<?> fluent) {
        fluent.accept(new ApplyApiLifecycle(lifecycle, force));
        fluent.accept(new ApplyComponentLifecycle(lifecycle, force));
    }
}
