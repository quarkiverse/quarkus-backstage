package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class EntityListBuilder extends EntityListFluent<EntityListBuilder>
        implements VisitableBuilder<EntityList, EntityListBuilder> {
    public EntityListBuilder() {
        this(new EntityList());
    }

    public EntityListBuilder(EntityListFluent<?> fluent) {
        this(fluent, new EntityList());
    }

    public EntityListBuilder(EntityListFluent<?> fluent, EntityList instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public EntityListBuilder(EntityList instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    EntityListFluent<?> fluent;

    public EntityList build() {
        EntityList buildable = new EntityList(fluent.buildItems());
        return buildable;
    }

}