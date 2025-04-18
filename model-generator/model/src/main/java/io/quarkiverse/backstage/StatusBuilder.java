package io.quarkiverse.backstage;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class StatusBuilder extends StatusFluent<StatusBuilder> implements VisitableBuilder<Status, StatusBuilder> {
    public StatusBuilder() {
        this(new Status());
    }

    public StatusBuilder(StatusFluent<?> fluent) {
        this(fluent, new Status());
    }

    public StatusBuilder(StatusFluent<?> fluent, Status instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public StatusBuilder(Status instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    StatusFluent<?> fluent;

    public Status build() {
        Status buildable = new Status(fluent.buildItems());
        return buildable;
    }

}