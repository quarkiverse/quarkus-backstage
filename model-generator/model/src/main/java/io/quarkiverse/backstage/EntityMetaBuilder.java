package io.quarkiverse.backstage;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class EntityMetaBuilder extends EntityMetaFluent<EntityMetaBuilder>
        implements VisitableBuilder<EntityMeta, EntityMetaBuilder> {
    public EntityMetaBuilder() {
        this(new EntityMeta());
    }

    public EntityMetaBuilder(EntityMetaFluent<?> fluent) {
        this(fluent, new EntityMeta());
    }

    public EntityMetaBuilder(EntityMetaFluent<?> fluent, EntityMeta instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public EntityMetaBuilder(EntityMeta instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    EntityMetaFluent<?> fluent;

    public EntityMeta build() {
        EntityMeta buildable = new EntityMeta(fluent.getUid(), fluent.getEtag(), fluent.getName(), fluent.getNamespace(),
                fluent.getTitle(), fluent.getDescription(), fluent.getLabels(), fluent.getAnnotations(), fluent.getTags(),
                fluent.buildLinks());
        return buildable;
    }

}