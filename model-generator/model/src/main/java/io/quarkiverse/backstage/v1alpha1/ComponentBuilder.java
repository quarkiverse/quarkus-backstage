package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class ComponentBuilder extends ComponentFluent<ComponentBuilder>
        implements VisitableBuilder<Component, ComponentBuilder> {
    public ComponentBuilder() {
        this(new Component());
    }

    public ComponentBuilder(ComponentFluent<?> fluent) {
        this(fluent, new Component());
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
        Component buildable = new Component(fluent.buildMetadata(), fluent.buildSpec(), fluent.buildStatus());
        return buildable;
    }

}