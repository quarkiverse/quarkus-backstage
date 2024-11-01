package io.quarkiverse.backstage.common.handlers;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkus.builder.BuildResult;

public class GetBackstageEntitiesAsStringHandler implements BiConsumer<Object, BuildResult> {

    @Override
    public void accept(Object context, BuildResult buildResult) {
        EntityListBuildItem entityListBuildItem = buildResult.consume(EntityListBuildItem.class);
        Consumer<String> consumer = (Consumer<String>) context;
        consumer.accept(Serialization.asYaml(entityListBuildItem.getEntityList()));
    }
}
