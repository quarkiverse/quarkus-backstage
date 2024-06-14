package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class MultilineApiDefintionBuilder extends MultilineApiDefintionFluent<MultilineApiDefintionBuilder>
        implements VisitableBuilder<MultilineApiDefintion, MultilineApiDefintionBuilder> {
    public MultilineApiDefintionBuilder() {
        this(new MultilineApiDefintion());
    }

    public MultilineApiDefintionBuilder(MultilineApiDefintionFluent<?> fluent) {
        this(fluent, new MultilineApiDefintion());
    }

    public MultilineApiDefintionBuilder(MultilineApiDefintionFluent<?> fluent, MultilineApiDefintion instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public MultilineApiDefintionBuilder(MultilineApiDefintion instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    MultilineApiDefintionFluent<?> fluent;

    public MultilineApiDefintion build() {
        MultilineApiDefintion buildable = new MultilineApiDefintion(fluent.getValue());
        return buildable;
    }

}