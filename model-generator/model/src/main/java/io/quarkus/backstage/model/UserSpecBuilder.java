package io.quarkus.backstage.model;

import io.quarkus.backstage.model.builder.VisitableBuilder;

public class UserSpecBuilder extends UserSpecFluent<UserSpecBuilder> implements VisitableBuilder<UserSpec, UserSpecBuilder> {
    public UserSpecBuilder() {
        this.fluent = this;
    }

    public UserSpecBuilder(UserSpecFluent<?> fluent) {
        this.fluent = fluent;
    }

    public UserSpecBuilder(UserSpecFluent<?> fluent, UserSpec instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public UserSpecBuilder(UserSpec instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    UserSpecFluent<?> fluent;

    public UserSpec build() {
        UserSpec buildable = new UserSpec(fluent.buildProfile(), fluent.getMemberOf(), fluent.getAdditionalProperties());
        return buildable;
    }

}