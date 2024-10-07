package io.quarkiverse.backstage.spi;

import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.builder.item.SimpleBuildItem;

public final class EntityListBuildItem extends SimpleBuildItem {

    private final EntityList entityList;

    public EntityListBuildItem(EntityList entityList) {
        this.entityList = entityList;
    }

    public EntityList getEntityList() {
        return entityList;
    }
}
