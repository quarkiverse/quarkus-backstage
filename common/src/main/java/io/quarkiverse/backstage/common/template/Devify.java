package io.quarkiverse.backstage.common.template;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.utils.Templates;
import io.quarkiverse.backstage.common.visitors.component.ApplyComponentAnnotation;
import io.quarkiverse.backstage.common.visitors.template.ApplyGiteaParameters;
import io.quarkiverse.backstage.common.visitors.template.ApplyPublishGiteaStep;
import io.quarkiverse.backstage.common.visitors.template.ApplyRegisterGiteaHostedComponentStep;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateBuilder;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkiverse.backstage.v1alpha1.EntityListBuilder;

public class Devify {

    private static final List<String> DEV_FRIENDLY_ACTIONS = List.of("featch:plain", "fetch:template", "publish:gitea",
            "catalog:register");

    private static final String DEFAULT_REPOSITORY_KEY = "repo";
    private static final String DEFAULT_REPOSITORY_HOST = "gitea:3000";

    private final String repositoryKey;
    private final String repositoryHost;
    private final List<String> devFriendlyActions;

    public Devify() {
        this(DEFAULT_REPOSITORY_KEY, DEFAULT_REPOSITORY_HOST, DEV_FRIENDLY_ACTIONS);
    }

    public Devify(String repositoryKey, String repositoryHost) {
        this(repositoryKey, repositoryHost, DEV_FRIENDLY_ACTIONS);
    }

    public Devify(String repositoryKey, String repositoryHost, List<String> devFriendlyActions) {
        this.repositoryKey = repositoryKey;
        this.repositoryHost = repositoryHost;
        this.devFriendlyActions = devFriendlyActions;
    }

    public Map<Path, String> devify(Map<Path, String> source) {
        Map<Path, String> result = new HashMap<>();
        Path templatePath = Templates.getTemplatePath(source);
        Path templateDir = templatePath.getParent();
        Path templatesDir = templateDir.getParent();
        String templateContent = source.get(templatePath);
        Template template = Serialization.unmarshal(templateContent, Template.class);
        System.out.println("Devifying template: " + template.getMetadata().getName());

        String templateName = template.getMetadata().getName();
        String devTemplateName = templateName + "-dev";
        Template devified = new TemplateBuilder(template)
                .editMetadata()
                .withDescription("A dev friendly version of the " + template.getMetadata().getName() + " template")
                .withName(devTemplateName)
                .addToAnnotations("backstage.io/child-of", template.getMetadata().getName())
                .addToTags("dev")
                .endMetadata()
                .accept(
                        new ApplyGiteaParameters(repositoryKey, repositoryHost),
                        new ApplyPublishGiteaStep(),
                        new ApplyRegisterGiteaHostedComponentStep())
                .build();

        Path devTemplatePath = templatesDir.resolve(devTemplateName);
        source.forEach((path, content) -> {
            if (!path.equals(templatePath)) {
                Path relative = templateDir.relativize(path);
                Path newPath = devTemplatePath.resolve(rename(relative, templateName, devTemplateName));
                result.put(newPath, content);
            }
        });
        result.put(devTemplatePath.resolve("template.yaml"), Serialization.asYaml(devified));

        // Find all catalog-info.yaml files and update the source location
        result.forEach((path, content) -> {
            if (path.getFileName().toString().equals("catalog-info.yaml")) {
                EntityList entityList = Serialization.unmarshalAsList(content);
                EntityList updatedEntityList = new EntityListBuilder(entityList)
                        .accept(new ApplyComponentAnnotation("backstage.io/source-location",
                                "url:http://" + "${{ values.repoHost }}/${{ values.repoOrg }}/${{ values.repoName }}"))
                        .build();
                result.put(path, Serialization.asYaml(updatedEntityList));
            }
        });

        return result;
    }

    private Path rename(Path p, String from, String to) {
        return Paths.get(p.toString().replaceAll(Pattern.quote(from), to));
    }
}
