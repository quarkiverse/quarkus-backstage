package io.quarkiverse.backstage.spi;

import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.builder.item.SimpleBuildItem;

public final class CatalogInstallationBuildItem extends SimpleBuildItem {

    private final EntityList entityList;
    private final String url;

    public CatalogInstallationBuildItem(EntityList entityList, String url) {
        this.entityList = entityList;
        this.url = url;
    }

    public EntityList getEntityList() {
        return entityList;
    }

    public String getUrl() {
        return url;
    }
}
