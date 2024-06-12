package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class GroupSpecBuilder extends GroupSpecFluent<GroupSpecBuilder>
        implements VisitableBuilder<GroupSpec, GroupSpecBuilder> {
    public GroupSpecBuilder() {
        this(new GroupSpec());
    }

    public GroupSpecBuilder(GroupSpecFluent<?> fluent) {
        this(fluent, new GroupSpec());
    }

    public GroupSpecBuilder(GroupSpecFluent<?> fluent, GroupSpec instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public GroupSpecBuilder(GroupSpec instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    GroupSpecFluent<?> fluent;

    public GroupSpec build() {
        GroupSpec buildable = new GroupSpec(fluent.getType(), fluent.buildProfile(), fluent.getParent(), fluent.getChildren(),
                fluent.getMembers(), fluent.getAdditionalProperties());
        return buildable;
    }

}