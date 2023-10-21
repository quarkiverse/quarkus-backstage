package io.quarkus.backstage.model;

import io.quarkus.backstage.model.builder.VisitableBuilder;

public class DomainBuilder extends DomainFluent<DomainBuilder> implements VisitableBuilder<Domain, DomainBuilder> {
    public DomainBuilder() {
        this.fluent = this;
    }

    public DomainBuilder(DomainFluent<?> fluent) {
        this.fluent = fluent;
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
        Domain buildable = new Domain(fluent.getKind(), fluent.getApiVersion(), fluent.buildMetadata(), fluent.buildSpec(),
                fluent.buildStatus());
        return buildable;
    }

}