package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class GroupBuilder extends GroupFluent<GroupBuilder> implements VisitableBuilder<Group, GroupBuilder> {
    public GroupBuilder() {
        this(new Group());
    }

    public GroupBuilder(GroupFluent<?> fluent) {
        this(fluent, new Group());
    }

    public GroupBuilder(GroupFluent<?> fluent, Group instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public GroupBuilder(Group instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    GroupFluent<?> fluent;

    public Group build() {
        Group buildable = new Group(fluent.getKind(), fluent.buildMetadata(), fluent.buildSpec(), fluent.buildStatus());
        return buildable;
    }

}