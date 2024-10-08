package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class UserBuilder extends UserFluent<UserBuilder> implements VisitableBuilder<User, UserBuilder> {
    public UserBuilder() {
        this(new User());
    }

    public UserBuilder(UserFluent<?> fluent) {
        this(fluent, new User());
    }

    public UserBuilder(UserFluent<?> fluent, User instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public UserBuilder(User instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    UserFluent<?> fluent;

    public User build() {
        User buildable = new User(fluent.getKind(), fluent.buildMetadata(), fluent.buildSpec(), fluent.buildStatus());
        return buildable;
    }

}