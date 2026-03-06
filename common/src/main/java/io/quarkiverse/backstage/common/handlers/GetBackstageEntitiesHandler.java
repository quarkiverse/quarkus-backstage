package io.quarkiverse.backstage.common.handlers;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

import io.quarkiverse.backstage.spi.CatalogInfoRequiredFileBuildItem;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.builder.BuildResult;

public class GetBackstageEntitiesHandler implements BiConsumer<Object, BuildResult> {

    @Override
    public void accept(Object context, BuildResult buildResult) {
        EntityListBuildItem entityListBuildItem = buildResult.consume(EntityListBuildItem.class);
        List<CatalogInfoRequiredFileBuildItem> catalogInfoRequiredFileBuildItem = buildResult
                .consumeMulti(CatalogInfoRequiredFileBuildItem.class);
        HandlerProcessor<EntityList> processor = (HandlerProcessor<EntityList>) context;
        processor.process(entityListBuildItem.getEntityList(),
                catalogInfoRequiredFileBuildItem.stream().map(CatalogInfoRequiredFileBuildItem::getPath).toArray(Path[]::new));
    }
}
