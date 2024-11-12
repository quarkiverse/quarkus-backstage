package io.quarkiverse.backstage.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.spi.TemplateBuildItem;

public final class Templates {

    private Templates() {
        //Utility class
    }

    public static TemplateBuildItem createTemplateBuildItem(Path sourceTemplateDir) {
        Map<Path, String> templateContent = new HashMap<>();
        Path templatePath = sourceTemplateDir.resolve("template.yaml");
        Template template = Serialization.unmarshal(templatePath.toFile(), Template.class);
        templateContent.put(templatePath, Serialization.asYaml(template));

        try {
            Files.list(sourceTemplateDir).forEach(p -> {
                if (!p.toFile().isDirectory()) {
                    templateContent.put(sourceTemplateDir.relativize(p), Strings.read(p));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new TemplateBuildItem(template, templateContent);
    }

    public static TemplateBuildItem move(TemplateBuildItem templateBuildItem, Path targetTemplateDir) {
        Optional<Path> templatePath = templateBuildItem.getContent().keySet().stream()
                .filter(p -> p.getFileName().toString().equals("template.yaml")).findFirst();
        Optional<Path> templateDirPath = templatePath.map(Path::getParent);
        Optional<Path> templatesSourcePath = templateDirPath.map(Path::getParent);
        return templatesSourcePath.map(dir -> move(templateBuildItem, dir, targetTemplateDir))
                .orElseThrow(() -> new IllegalArgumentException("No common parrent found"));
    }

    public static TemplateBuildItem move(TemplateBuildItem templateBuildItem, Path sourceTemplateDir,
            Path targetTemplateDir) {
        Map<Path, String> templateContent = new HashMap<>();
        templateBuildItem.getContent().forEach((path, content) -> {
            if (path.isAbsolute() && path.startsWith(sourceTemplateDir)) {
                templateContent.put(targetTemplateDir.resolve(sourceTemplateDir.relativize(path)), content);
            } else {
                templateContent.put(targetTemplateDir.resolve(path), content);
            }
        });
        return new TemplateBuildItem(templateBuildItem.getTemplate(), templateContent);
    }
}
