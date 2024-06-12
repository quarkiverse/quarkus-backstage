package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class SystemSpecBuilder extends SystemSpecFluent<SystemSpecBuilder>
        implements VisitableBuilder<SystemSpec, SystemSpecBuilder> {
    public SystemSpecBuilder() {
        this.fluent = this;
    }

    public SystemSpecBuilder(SystemSpecFluent<?> fluent) {
        this.fluent = fluent;
    }

    public SystemSpecBuilder(SystemSpecFluent<?> fluent, SystemSpec instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public SystemSpecBuilder(SystemSpec instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    SystemSpecFluent<?> fluent;

    public SystemSpec build() {
        SystemSpec buildable = new SystemSpec(fluent.getOwner(), fluent.getDomain(), fluent.getAdditionalProperties());
        return buildable;
    }

}