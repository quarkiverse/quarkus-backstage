package io.quarkiverse.backstage.common.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

        Consumer<List<TemplateBuildItem>> consumer = (Consumer<List<TemplateBuildItem>>) context;
        consumer.accept(allTemplateBuildItems);
    }
}
