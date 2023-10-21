package io.quarkus.backstage.model;

import io.quarkus.backstage.model.builder.VisitableBuilder;

public class EntityLinkBuilder extends EntityLinkFluent<EntityLinkBuilder>
        implements VisitableBuilder<EntityLink, EntityLinkBuilder> {
    public EntityLinkBuilder() {
        this.fluent = this;
    }

    public EntityLinkBuilder(EntityLinkFluent<?> fluent) {
        this.fluent = fluent;
    }

    public EntityLinkBuilder(EntityLinkFluent<?> fluent, EntityLink instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public EntityLinkBuilder(EntityLink instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    EntityLinkFluent<?> fluent;

    public EntityLink build() {
        EntityLink buildable = new EntityLink(fluent.getUrl(), fluent.getTitle(), fluent.getIcon(), fluent.getType());
        return buildable;
    }

}