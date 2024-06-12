package io.quarkiverse.backstage.deployment.visitors;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentFluent;

public class ApplyComponentName extends TypedVisitor<ComponentFluent<?>> {

    private final String projectName;

    public ApplyComponentName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public void visit(ComponentFluent<?> component) {
        component.accept(new ApplyMetadataName(projectName));
    }
}
