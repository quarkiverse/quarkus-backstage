package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class DomainBuilder extends DomainFluent<DomainBuilder> implements VisitableBuilder<Domain, DomainBuilder> {
    public DomainBuilder() {
        this(new Domain());
    }

    public DomainBuilder(DomainFluent<?> fluent) {
        this(fluent, new Domain());
    }

    public DomainBuilder(DomainFluent<?> fluent, Domain instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public DomainBuilder(Domain instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    DomainFluent<?> fluent;

    public Domain build() {
        Domain buildable = new Domain(fluent.getKind(), fluent.buildMetadata(), fluent.buildSpec(), fluent.buildStatus());
        return buildable;
    }

}