package io.quarkiverse.backstage.cli.handlers;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.builder.BuildResult;

public class GetBackstageEntitiesHandler implements BiConsumer<Object, BuildResult> {

    @Override
    public void accept(Object context, BuildResult buildResult) {
        EntityListBuildItem entityListBuildItem = buildResult.consume(EntityListBuildItem.class);
        Consumer<EntityList> consumer = (Consumer<EntityList>) context;
        consumer.accept(entityListBuildItem.getEntityList());
    }
}
