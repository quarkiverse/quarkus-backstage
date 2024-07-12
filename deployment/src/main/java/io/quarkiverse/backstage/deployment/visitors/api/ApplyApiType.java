package io.quarkiverse.backstage.deployment.visitors.api;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ApiSpecFluent;

public class ApplyApiType extends TypedVisitor<ApiSpecFluent<?>> {

    private final String type;
    private final boolean force;

    public ApplyApiType(String type) {
        this(type, false);
    }

    public ApplyApiType(String type, boolean force) {
        this.type = type;
        this.force = force;
    }

    @Override
    public void visit(ApiSpecFluent<?> spec) {
        if (force || !spec.hasType()) {
            spec.withType(type);
        }
    }
}
