package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class ComponentSpecBuilder extends ComponentSpecFluent<ComponentSpecBuilder>
        implements VisitableBuilder<ComponentSpec, ComponentSpecBuilder> {
    public ComponentSpecBuilder() {
        this(new ComponentSpec());
    }

    public ComponentSpecBuilder(ComponentSpecFluent<?> fluent) {
        this(fluent, new ComponentSpec());
    }

    public ComponentSpecBuilder(ComponentSpecFluent<?> fluent, ComponentSpec instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public ComponentSpecBuilder(ComponentSpec instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    ComponentSpecFluent<?> fluent;

    public ComponentSpec build() {
        ComponentSpec buildable = new ComponentSpec(fluent.getType(), fluent.getLifecycle(), fluent.getOwner(),
                fluent.getSystem(), fluent.getSubcomponentOf(), fluent.getProvidesApis(), fluent.getConsumesApis(),
                fluent.getDependsOn(), fluent.getAdditionalProperties());
        return buildable;
    }

}