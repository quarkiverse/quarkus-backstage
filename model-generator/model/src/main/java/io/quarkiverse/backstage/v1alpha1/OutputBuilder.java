package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class OutputBuilder extends OutputFluent<OutputBuilder> implements VisitableBuilder<Output, OutputBuilder> {
    public OutputBuilder() {
        this(new Output());
    }

    public OutputBuilder(OutputFluent<?> fluent) {
        this(fluent, new Output());
    }

    public OutputBuilder(OutputFluent<?> fluent, Output instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public OutputBuilder(Output instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    OutputFluent<?> fluent;

    public Output build() {
        Output buildable = new Output(fluent.getName(), fluent.getDescription());
        return buildable;
    }

}