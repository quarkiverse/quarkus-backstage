package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class SystemBuilder extends SystemFluent<SystemBuilder> implements VisitableBuilder<System, SystemBuilder> {
    public SystemBuilder() {
        this(new System());
    }

    public SystemBuilder(SystemFluent<?> fluent) {
        this(fluent, new System());
    }

    public SystemBuilder(SystemFluent<?> fluent, System instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public SystemBuilder(System instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    SystemFluent<?> fluent;

    public System build() {
        System buildable = new System(fluent.buildMetadata(), fluent.buildSpec(), fluent.buildStatus());
        return buildable;
    }

}