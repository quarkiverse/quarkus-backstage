package io.quarkiverse.backstage.common.visitors;

import io.quarkiverse.backstage.common.visitors.api.ApplyApiOwner;
import io.quarkiverse.backstage.common.visitors.component.ApplyComponentOwner;
import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.TypedVisitor;

public class ApplyOwner extends TypedVisitor<BaseFluent<?>> {

    private final String owner;
    private final boolean force;

    public ApplyOwner(String owner) {
        this(owner, false);
    }

    public ApplyOwner(String owner, boolean force) {
        this.owner = owner;
        this.force = force;
    }

    @Override
    public void visit(BaseFluent<?> fluent) {
        fluent.accept(new ApplyApiOwner(owner, force));
        fluent.accept(new ApplyComponentOwner(owner, force));
    }
}
