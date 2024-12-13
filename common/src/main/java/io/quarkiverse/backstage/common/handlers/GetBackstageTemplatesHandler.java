package io.quarkiverse.backstage.common.handlers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import io.quarkiverse.backstage.spi.CatalogInfoRequiredFileBuildItem;
import io.quarkiverse.backstage.spi.DevTemplateBuildItem;
import io.quarkiverse.backstage.spi.TemplateBuildItem;
import io.quarkus.builder.BuildResult;

public class GetBackstageTemplatesHandler implements BiConsumer<Object, BuildResult> {

    @Override
    public void accept(Object context, BuildResult buildResult) {
        List<TemplateBuildItem> templateBuildItems = buildResult.consumeMulti(TemplateBuildItem.class);
        List<DevTemplateBuildItem> devTemplateBuildItems = buildResult.consumeMulti(DevTemplateBuildItem.class);
        List<TemplateBuildItem> allTemplateBuildItems = new ArrayList<>();
        allTemplateBuildItems.addAll(templateBuildItems);
        allTemplateBuildItems.addAll(
                devTemplateBuildItems.stream().map(DevTemplateBuildItem::toTemplateBuildItem).collect(Collectors.toList()));

        HandlerProcessor<List<TemplateBuildItem>> processor = (HandlerProcessor<List<TemplateBuildItem>>) context;
        List<CatalogInfoRequiredFileBuildItem> catalogInfoRequiredFileBuildItem = buildResult
                .consumeMulti(CatalogInfoRequiredFileBuildItem.class);
        processor.process(allTemplateBuildItems,
                catalogInfoRequiredFileBuildItem.stream().map(CatalogInfoRequiredFileBuildItem::getPath).toArray(Path[]::new));
    }
}
