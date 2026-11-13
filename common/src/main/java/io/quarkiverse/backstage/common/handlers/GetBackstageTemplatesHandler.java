package io.quarkiverse.backstage.common.handlers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.spi.CatalogInfoRequiredFileBuildItem;
import io.quarkiverse.backstage.spi.DevTemplateBuildItem;
import io.quarkiverse.backstage.spi.TemplateBuildItem;
import io.quarkiverse.helm.spi.HelmChartBuildItem;
import io.quarkus.builder.BuildResult;
import io.quarkus.deployment.builditem.GeneratedFileSystemResourceBuildItem;
import io.quarkus.kubernetes.spi.GeneratedKubernetesResourceBuildItem;

/**
 * Passes the generated templates to the context as a list of maps (see {@link #fromPayload(List)}).
 */
public class GetBackstageTemplatesHandler implements BiConsumer<Object, BuildResult> {

    @Override
    public void accept(Object context, BuildResult buildResult) {
        List<TemplateBuildItem> templateBuildItems = buildResult.consumeMulti(TemplateBuildItem.class);
        List<DevTemplateBuildItem> devTemplateBuildItems = buildResult.consumeMulti(DevTemplateBuildItem.class);
        List<TemplateBuildItem> allTemplateBuildItems = new ArrayList<>();
        List<GeneratedFileSystemResourceBuildItem> generatedFileSystemResourceBuildItems = buildResult
                .consumeMulti(GeneratedFileSystemResourceBuildItem.class);
        List<GeneratedKubernetesResourceBuildItem> generatedKubernetesResourceBuildItems = buildResult
                .consumeMulti(GeneratedKubernetesResourceBuildItem.class);
        List<HelmChartBuildItem> generatedHelmChartBuildItems = buildResult.consumeMulti(HelmChartBuildItem.class);

        Path rootDir = Projects.getProjectRoot();
        Path dotHelmDir = rootDir.resolve(".helm");

        for (HelmChartBuildItem helmChartBuildItem : generatedHelmChartBuildItems) {
            helmChartBuildItem.write(dotHelmDir, o -> Serialization.asYaml(o));
        }

        allTemplateBuildItems.addAll(templateBuildItems);
        allTemplateBuildItems.addAll(
                devTemplateBuildItems.stream().map(DevTemplateBuildItem::toTemplateBuildItem).collect(Collectors.toList()));

        BiConsumer<List<Map<String, Object>>, Path[]> processor = (BiConsumer<List<Map<String, Object>>, Path[]>) context;
        List<CatalogInfoRequiredFileBuildItem> catalogInfoRequiredFileBuildItem = buildResult
                .consumeMulti(CatalogInfoRequiredFileBuildItem.class);
        processor.accept(toPayload(allTemplateBuildItems),
                catalogInfoRequiredFileBuildItem.stream().map(CatalogInfoRequiredFileBuildItem::getPath).toArray(Path[]::new));
    }

    public static List<Map<String, Object>> toPayload(List<TemplateBuildItem> templateBuildItems) {
        List<Map<String, Object>> payload = new ArrayList<>();
        for (TemplateBuildItem templateBuildItem : templateBuildItems) {
            Map<String, Object> item = new HashMap<>();
            item.put("template", Serialization.asYaml(templateBuildItem.getTemplate()));
            item.put("content", new HashMap<>(templateBuildItem.getContent()));
            payload.add(item);
        }
        return payload;
    }

    @SuppressWarnings("unchecked")
    public static List<TemplateBuildItem> fromPayload(List<Map<String, Object>> payload) {
        List<TemplateBuildItem> templateBuildItems = new ArrayList<>();
        for (Map<String, Object> item : payload) {
            Template template = Serialization.unmarshal((String) item.get("template"), Template.class);
            templateBuildItems.add(new TemplateBuildItem(template, (Map<Path, String>) item.get("content")));
        }
        return templateBuildItems;
    }
}
