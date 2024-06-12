package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class UserSpecBuilder extends UserSpecFluent<UserSpecBuilder> implements VisitableBuilder<UserSpec, UserSpecBuilder> {
    public UserSpecBuilder() {
        this(new UserSpec());
    }

    public UserSpecBuilder(UserSpecFluent<?> fluent) {
        this(fluent, new UserSpec());
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