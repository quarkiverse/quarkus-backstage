package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class MultilineApiDefinitionBuilder extends MultilineApiDefinitionFluent<MultilineApiDefinitionBuilder>
        implements VisitableBuilder<MultilineApiDefinition, MultilineApiDefinitionBuilder> {
    public MultilineApiDefinitionBuilder() {
        this(new MultilineApiDefinition());
    }

    public MultilineApiDefinitionBuilder(MultilineApiDefinitionFluent<?> fluent) {
        this(fluent, new MultilineApiDefinition());
    }

    public MultilineApiDefinitionBuilder(MultilineApiDefinitionFluent<?> fluent, MultilineApiDefinition instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public MultilineApiDefinitionBuilder(MultilineApiDefinition instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    MultilineApiDefinitionFluent<?> fluent;

    public MultilineApiDefinition build() {
        MultilineApiDefinition buildable = new MultilineApiDefinition(fluent.getValue());
        return buildable;
    }

}