package io.quarkiverse.backstage.scaffolder.v1beta3;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class StepBuilder extends StepFluent<StepBuilder> implements VisitableBuilder<Step, StepBuilder> {
    public StepBuilder() {
        this(new Step());
    }

    public StepBuilder(StepFluent<?> fluent) {
        this(fluent, new Step());
    }

    public StepBuilder(StepFluent<?> fluent, Step instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public StepBuilder(Step instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    StepFluent<?> fluent;

    public Step build() {
        Step buildable = new Step(fluent.getId(), fluent.getName(), fluent.getAction(), fluent.getInput());
        return buildable;
    }

}