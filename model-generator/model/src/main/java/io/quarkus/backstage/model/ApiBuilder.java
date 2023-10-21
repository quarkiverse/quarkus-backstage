package io.quarkus.backstage.model;

import io.quarkus.backstage.model.builder.VisitableBuilder;

public class ApiBuilder extends ApiFluent<ApiBuilder> implements VisitableBuilder<Api, ApiBuilder> {
    public ApiBuilder() {
        this.fluent = this;
    }

    public ApiBuilder(ApiFluent<?> fluent) {
        this.fluent = fluent;
    }

    public ApiBuilder(ApiFluent<?> fluent, Api instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public ApiBuilder(Api instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    ApiFluent<?> fluent;

    public Api build() {
        Api buildable = new Api(fluent.getKind(), fluent.getApiVersion(), fluent.buildSpec(), fluent.buildStatus());
        return buildable;
    }

}