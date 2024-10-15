package io.quarkiverse.backstage.deployment.template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import io.quarkiverse.backstage.deployment.utils.*;
import io.quarkiverse.backstage.deployment.visitors.*;
import io.quarkiverse.backstage.deployment.visitors.component.*;
import io.quarkiverse.backstage.deployment.visitors.template.*;
import io.quarkiverse.backstage.model.builder.Visitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.PropertyBuilder;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateBuilder;

public class TemplateGenerator {

    private Path projectDirPath;
    private String name;
    private String namespace;

    public TemplateGenerator(Path projectDirPath, String name, String namespace) {
        this.projectDirPath = projectDirPath;
        this.name = name;
        this.namespace = namespace;
    }

    public Map<Path, String> generate() {
        Optional<String> basePackage = Packages.findCommonPackagePrefix(projectDirPath);
        Map<String, String> parameters = new HashMap<>();
        parameters.putAll(Projects.getProjectInfo(projectDirPath));
        parameters.put("componentId", parameters.getOrDefault("artifactId", name));
        basePackage.ifPresent(p -> parameters.put("package", p));

        String templateName = parameters.getOrDefault("artifactId", "my-template");
        List<Visitor> visitors = new ArrayList<>();

        visitors.add(new AddNewEntityRefToOutput("Open the catalog info", "${{ steps.register.output.entityRef }}"));

        visitors.add(new AddNewTemplateParameter("Component configuration",
                new PropertyBuilder()
                        .withName("componentId")
                        .withTitle("Component ID")
                        .withDescription(
                                "The ID of the software component. This will be used as the name of the git repository and component.")
                        .withType("string")
                        .withDefaultValue("my-app")
                        .withRequired(true)
                        .build()));

        visitors.add(new AddNewTemplateParameter("Project details",
                new PropertyBuilder()
                        .withName("groupId")
                        .withTitle("Group ID")
                        .withDescription("The group ID of the project")
                        .withType("string")
                        .withDefaultValue("org.acme")
                        .withRequired(true)
                        .build(),

                new PropertyBuilder()
                        .withName("artifactId")
                        .withTitle("Artifact ID")
                        .withDescription("The artifact ID of the project")
                        .withType("string")
                        .withDefaultValue("code-with-quarkus")
                        .withRequired(true)
                        .build(),

                new PropertyBuilder()
                        .withName("version")
                        .withTitle("Version")
                        .withDescription("The version of the project")
                        .withType("string")
                        .withDefaultValue("1.0-SNAPSHOT")
                        .withRequired(true)
                        .build(),

                new PropertyBuilder()
                        .withName("name")
                        .withTitle("Name")
                        .withDescription("The name of the project")
                        .withType("string")
                        .build(),

                new PropertyBuilder()
                        .withName("description")
                        .withTitle("Description")
                        .withDescription("The description of the project")
                        .withType("string")
                        .build(),

                new PropertyBuilder()
                        .withName("package")
                        .withTitle("Package")
                        .withDescription("The base package of the project")
                        .withType("string")
                        .withDefaultValue("org.acme")
                        .build()

        ));

        visitors.add(new AddNewTemplateParameter("Git repository configuration",
                new PropertyBuilder()
                        .withName("repo")
                        .withType("object")
                        .withTitle("Repository Configuration")
                        .withRequired(true)
                        .withProperties(Map.of(
                                "host", new PropertyBuilder()
                                        .withName("host")
                                        .withTitle("Host")
                                        .withDescription("The host of the git repository")
                                        .withType("string")
                                        .withDefaultValue("github.com")
                                        .withRequired(true)
                                        .build(),
                                "org", new PropertyBuilder()
                                        .withName("org")
                                        .withTitle("Organization")
                                        .withDescription("The organization of the git repository")
                                        .withType("string")
                                        .withDefaultValue("my-org")
                                        .withRequired(true)
                                        .build(),
                                "visibility", new PropertyBuilder()
                                        .withName("visibility")
                                        .withTitle("Visibility")
                                        .withDescription("The visibility of the git repository")
                                        .withType("string")
                                        .withDefaultValue("public")
                                        .withRequired(true)
                                        .build()))
                        .build()));

        visitors.add(new ApplyMetadataTag("java"));
        visitors.add(new ApplyMetadataTag("quarkus"));

        //Render the app skeleton
        visitors.add(new AddNewFetchTemplateStep("render", "skeleton/", true, List.of(), parameters));
        visitors.add(new AddPublishGithubStep("publish"));
        visitors.add(new AddRegisterComponentStep("register"));

        Template template = new TemplateBuilder()
                .withNewMetadata()
                .withName(templateName)
                .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                .withType("service")
                .withNewOutput()
                .endOutput()
                .endSpec()
                .accept(visitors.toArray(new Visitor[visitors.size()]))
                .build();

        String templateContent = Serialization.asYaml(template);
        Path backstageDir = projectDirPath.resolve(".backstage");
        Path templatesDir = backstageDir.resolve("templates");
        Path templateDir = templatesDir.resolve(templateName);

        Path skeletonDir = templateDir.resolve("skeleton");
        Path templateYamlPath = templateDir.resolve("template.yaml");

        Map<Path, String> content = new HashMap<>();
        content.put(templateYamlPath, templateContent);

        Path srcMainJavaDir = projectDirPath.resolve("src").resolve("main").resolve("java");
        Path srcMainResourcesDir = projectDirPath.resolve("src").resolve("main").resolve("resources");
        Path srcTestJavaDir = projectDirPath.resolve("src").resolve("test").resolve("java");
        Path srcTestResourcesDir = projectDirPath.resolve("src").resolve("test").resolve("resources");

        Path destMainJavaBase = skeletonDir.resolve("src").resolve("main").resolve("java").resolve("${{ values.package }}");
        Path destMainResourcesBase = skeletonDir.resolve("src").resolve("main").resolve("resources");
        Path destTestJavaBase = skeletonDir.resolve("src").resolve("test").resolve("java").resolve("${{ values.package }}");
        Path destTestResourcesBase = skeletonDir.resolve("src").resolve("test").resolve("resources");

        content.putAll(
                SourceTransformer.transform(srcMainJavaDir, destMainJavaBase.getParent(), parameters, basePackage.orElse("")));
        content.putAll(SourceTransformer.transform(srcMainResourcesDir, destMainResourcesBase.getParent(), parameters,
                basePackage.orElse("")));
        content.putAll(
                SourceTransformer.transform(srcTestJavaDir, destTestJavaBase.getParent(), parameters, basePackage.orElse("")));
        content.putAll(SourceTransformer.transform(srcTestResourcesDir, destTestResourcesBase.getParent(), parameters,
                basePackage.orElse("")));

        content.putAll(createSkeletonContent(skeletonDir, projectDirPath.resolve("catalog-info.yaml"), parameters));
        content.putAll(createSkeletonContent(skeletonDir, projectDirPath.resolve("README.md"), parameters));
        content.putAll(createSkeletonContent(skeletonDir, projectDirPath.resolve("readme.md"), parameters));
        content.putAll(createSkeletonContent(skeletonDir, projectDirPath.resolve("pom.xml"), parameters));
        content.putAll(createSkeletonContent(skeletonDir, projectDirPath.resolve("settings.gradle"), parameters));
        content.putAll(createSkeletonContent(skeletonDir, projectDirPath.resolve("settings.gradle.kts"), parameters));

        return content;
    }

    private void createSkeleton(Path skeletonDir, Path path, Map<String, String> parameters) {
        try {
            String content = Files.readString(path);
            Path targetPath = toSkeletonPath(skeletonDir, path);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                content = parameterize(content, entry.getKey(), entry.getValue());
            }
            writeStringSafe(targetPath, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    private Map<Path, String> createSkeletonContent(Path skeletonDir, Path path, Map<String, String> parameters) {
        try {
            if (!path.toFile().exists()) {
                return Map.of();
            }
            String content = Files.readString(path);
            Path targetPath = toSkeletonPath(skeletonDir, path);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                content = parameterize(content, entry.getKey(), entry.getValue());
            }
            return Map.of(targetPath, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    private Path toSkeletonPath(Path skeletonDir, Path file) {
        return skeletonDir.resolve(projectDirPath.relativize(file));
    }

    private String parameterize(String content, String name, String value) {
        if (value == null) {
            System.err.println("Value for " + name + " is null. Ignoring.");
            return content;
        }
        String placeholder = "\\$\\{\\{ values\\." + name + " \\}\\}";
        return content.contains(value) ? content.replaceAll(Pattern.quote(value), placeholder) : content;
    }

    private void writeStringSafe(Path p, String content) {
        try {
            Files.writeString(p, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
