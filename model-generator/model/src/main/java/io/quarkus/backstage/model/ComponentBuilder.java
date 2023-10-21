package io.quarkus.backstage.model;

import io.quarkus.backstage.model.builder.VisitableBuilder;

public class ComponentBuilder extends ComponentFluent<ComponentBuilder>
        implements VisitableBuilder<Component, ComponentBuilder> {
    public ComponentBuilder() {
        this.fluent = this;
    }

    public ComponentBuilder(ComponentFluent<?> fluent) {
        this.fluent = fluent;
    }

    public ComponentBuilder(ComponentFluent<?> fluent, Component instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public ComponentBuilder(Component instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    ComponentFluent<?> fluent;

    public Component build() {
        Component buildable = new Component(fluent.getKind(), fluent.getApiVersion(), fluent.buildSpec(), fluent.buildStatus());
        return buildable;
    }

}