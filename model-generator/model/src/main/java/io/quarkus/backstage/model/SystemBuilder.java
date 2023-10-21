package io.quarkus.backstage.model;

import io.quarkus.backstage.model.builder.VisitableBuilder;

public class SystemBuilder extends SystemFluent<SystemBuilder> implements VisitableBuilder<System, SystemBuilder> {
    public SystemBuilder() {
        this.fluent = this;
    }

    public SystemBuilder(SystemFluent<?> fluent) {
        this.fluent = fluent;
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
        System buildable = new System(fluent.getKind(), fluent.getApiVersion(), fluent.buildMetadata(), fluent.buildSpec(),
                fluent.buildStatus());
        return buildable;
    }

}