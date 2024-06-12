package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class StatusItemBuilder extends StatusItemFluent<StatusItemBuilder>
        implements VisitableBuilder<StatusItem, StatusItemBuilder> {
    public StatusItemBuilder() {
        this.fluent = this;
    }

    public StatusItemBuilder(StatusItemFluent<?> fluent) {
        this.fluent = fluent;
    }

    public StatusItemBuilder(StatusItemFluent<?> fluent, StatusItem instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public StatusItemBuilder(StatusItem instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    StatusItemFluent<?> fluent;

    public StatusItem build() {
        StatusItem buildable = new StatusItem(fluent.getType(), fluent.getLevel(), fluent.getMessage(), fluent.getError());
        return buildable;
    }

}