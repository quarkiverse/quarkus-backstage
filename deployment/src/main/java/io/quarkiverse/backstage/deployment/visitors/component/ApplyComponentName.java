package io.quarkiverse.backstage.deployment.visitors.component;

import io.quarkiverse.backstage.deployment.visitors.ApplyMetadataName;
import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentFluent;

public class ApplyComponentName extends TypedVisitor<ComponentFluent<?>> {

    private final String name;

    public ApplyComponentName(String name) {
        this.name = name;
    }

    @Override
    public void visit(ComponentFluent<?> component) {
        component.accept(new ApplyMetadataName(name));
    }
}
