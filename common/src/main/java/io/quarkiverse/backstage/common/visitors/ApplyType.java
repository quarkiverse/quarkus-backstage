package io.quarkiverse.backstage.common.visitors;

import io.quarkiverse.backstage.common.visitors.api.ApplyApiType;
import io.quarkiverse.backstage.common.visitors.component.ApplyComponentType;
import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.TypedVisitor;

public class ApplyType extends TypedVisitor<BaseFluent<?>> {

    private final String type;
    private final boolean force;

    public ApplyType(String type) {
        this(type, false);
    }

    public ApplyType(String type, boolean force) {
        this.type = type;
        this.force = force;
    }

    @Override
    public void visit(BaseFluent<?> fluent) {
        fluent.accept(new ApplyApiType(type, force));
        fluent.accept(new ApplyComponentType(type, force));
    }
}
