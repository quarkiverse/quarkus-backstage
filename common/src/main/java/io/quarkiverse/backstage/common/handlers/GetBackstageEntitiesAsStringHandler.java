package io.quarkiverse.backstage.common.handlers;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.spi.CatalogInfoRequiredFileBuildItem;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkus.builder.BuildResult;

public class GetBackstageEntitiesAsStringHandler implements BiConsumer<Object, BuildResult> {

    @Override
    public void accept(Object context, BuildResult buildResult) {
        EntityListBuildItem entityListBuildItem = buildResult.consume(EntityListBuildItem.class);
        List<CatalogInfoRequiredFileBuildItem> catalogInfoRequiredFileBuildItem = buildResult
                .consumeMulti(CatalogInfoRequiredFileBuildItem.class);
        BiConsumer<String, Path[]> processor = (BiConsumer<String, Path[]>) context;
        processor.accept(Serialization.asYaml(entityListBuildItem.getEntityList()),
                catalogInfoRequiredFileBuildItem.stream().map(CatalogInfoRequiredFileBuildItem::getPath).toArray(Path[]::new));
    }
}
