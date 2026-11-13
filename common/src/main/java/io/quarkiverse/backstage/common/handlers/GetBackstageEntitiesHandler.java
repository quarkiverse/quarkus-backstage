package io.quarkiverse.backstage.common.handlers;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.spi.CatalogInfoRequiredFileBuildItem;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.builder.BuildResult;

/**
 * Passes the generated entities to the context as a YAML string (see {@link #fromPayload(String)}).
 */
public class GetBackstageEntitiesHandler implements BiConsumer<Object, BuildResult> {

    @Override
    public void accept(Object context, BuildResult buildResult) {
        EntityListBuildItem entityListBuildItem = buildResult.consume(EntityListBuildItem.class);
        List<CatalogInfoRequiredFileBuildItem> catalogInfoRequiredFileBuildItem = buildResult
                .consumeMulti(CatalogInfoRequiredFileBuildItem.class);
        BiConsumer<String, Path[]> processor = (BiConsumer<String, Path[]>) context;
        processor.accept(toPayload(entityListBuildItem.getEntityList()),
                catalogInfoRequiredFileBuildItem.stream().map(CatalogInfoRequiredFileBuildItem::getPath).toArray(Path[]::new));
    }

    public static String toPayload(EntityList entityList) {
        return Serialization.asYaml(entityList);
    }

    public static EntityList fromPayload(String payload) {
        return Serialization.unmarshalAsList(payload);
    }
}
