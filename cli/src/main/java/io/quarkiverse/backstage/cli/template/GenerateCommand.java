package io.quarkiverse.backstage.cli.template;

import static io.quarkiverse.backstage.cli.utils.Projects.getProjectInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.cli.utils.Directories;
import io.quarkiverse.backstage.cli.utils.Packages;
import io.quarkiverse.backstage.cli.utils.SourceTransformer;
import io.quarkiverse.backstage.deployment.utils.Serialization;
import io.quarkiverse.backstage.deployment.visitors.ApplyMetadataTag;
import io.quarkiverse.backstage.deployment.visitors.template.AddNewEntityRefToOutput;
import io.quarkiverse.backstage.deployment.visitors.template.AddNewFetchTemplateStep;
import io.quarkiverse.backstage.deployment.visitors.template.AddNewTemplateParameter;
import io.quarkiverse.backstage.deployment.visitors.template.AddPublishGithubStep;
import io.quarkiverse.backstage.deployment.visitors.template.AddRegisterComponentStep;
import io.quarkiverse.backstage.model.builder.Visitor;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.scaffolder.v1beta3.PropertyBuilder;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateBuilder;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generate", sortOptions = false, mixinStandardHelpOptions = false, header = "Generate Backstage Template.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class GenerateCommand extends GenerationBaseCommand {

    @Option(names = { "--name" }, description = "The template name")
    Optional<String> name = Optional.empty();

    public GenerateCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public void process(EntityList entityList) {
        List<TemplateListItem> items = new ArrayList<>();

        saveCatalogInfo(entityList);

        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        Optional<String> basePackage = Packages.findCommonPackagePrefix(project.getProjectDirPath());
        Map<String, String> parameters = new HashMap<>();
        parameters.putAll(getProjectInfo(project));
        parameters.put("componentId", parameters.getOrDefault("artifactId", "my-app"));
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
                .withNamespace(getNamespace())
                .endMetadata()
                .withNewSpec()
                .withType("service")
                .withNewOutput()
                .endOutput()
                .endSpec()
                .accept(visitors.toArray(new Visitor[visitors.size()]))
                .build();

        String templateContent = Serialization.asYaml(template);
        Path backstageDir = getWorkingDirectory().resolve(".backstage");
        Path templatesDir = backstageDir.resolve("templates");
        Path templateDir = templatesDir.resolve(templateName);

        if (templateDir.toFile().exists() && !Directories.delete(templateDir)) {
            throw new RuntimeException("Failed to create backstage template directory.");
        }

        Path skeletonDir = templateDir.resolve("skeleton");
        Path templateYamlPath = templateDir.resolve("template.yaml");

        Path srcMainJavaDir = project.getProjectDirPath().resolve("src").resolve("main").resolve("java");
        Path srcMainResourcesDir = project.getProjectDirPath().resolve("src").resolve("main").resolve("resources");
        Path srcTestJavaDir = project.getProjectDirPath().resolve("src").resolve("test").resolve("java");
        Path srcTestResourcesDir = project.getProjectDirPath().resolve("src").resolve("test").resolve("resources");

        Path destMainJavaBase = skeletonDir.resolve("src").resolve("main").resolve("java").resolve("${{ values.package }}");
        Path destMainResourcesBase = skeletonDir.resolve("src").resolve("main").resolve("resources");
        Path destTestJavaBase = skeletonDir.resolve("src").resolve("test").resolve("java").resolve("${{ values.package }}");
        Path destTestResourcesBase = skeletonDir.resolve("src").resolve("test").resolve("resources");

        if (!destMainJavaBase.toFile().exists() && !destMainJavaBase.toFile().mkdirs()) {
            throw new RuntimeException("Failed to create backstage template source package directory.");
        }

        if (!destMainResourcesBase.toFile().exists() && !destMainResourcesBase.toFile().mkdirs()) {
            throw new RuntimeException("Failed to create backstage template source package directory.");
        }

        if (!destTestJavaBase.toFile().exists() && !destTestJavaBase.toFile().mkdirs()) {
            throw new RuntimeException("Failed to create backstage template test package directory.");
        }

        if (!destTestResourcesBase.toFile().exists() && !destTestResourcesBase.toFile().mkdirs()) {
            throw new RuntimeException("Failed to create backstage template test package directory.");
        }

        SourceTransformer.copy(srcMainJavaDir, destMainJavaBase.getParent(), parameters, basePackage.orElse(""));
        SourceTransformer.copy(srcMainResourcesDir, destMainResourcesBase.getParent(), parameters, basePackage.orElse(""));
        SourceTransformer.copy(srcTestJavaDir, destTestJavaBase.getParent(), parameters, basePackage.orElse(""));
        SourceTransformer.copy(srcTestResourcesDir, destTestResourcesBase.getParent(), parameters, basePackage.orElse(""));

        createSkeletonIfExsists(skeletonDir, getWorkingDirectory().resolve("catalog-info.yaml"), parameters);
        createSkeletonIfExsists(skeletonDir, getWorkingDirectory().resolve("README.md"), parameters);
        createSkeletonIfExsists(skeletonDir, getWorkingDirectory().resolve("readme.md"), parameters);

        switch (project.getBuildTool()) {
            case MAVEN:
                createSkeleton(skeletonDir, getWorkingDirectory().resolve("pom.xml"), parameters);
                break;
            case GRADLE:
                createSkeleton(skeletonDir, getWorkingDirectory().resolve("build.gradle"), parameters);
                break;
            case GRADLE_KOTLIN_DSL:
                createSkeleton(skeletonDir, getWorkingDirectory().resolve("build.gradle.kts"), parameters);
                break;
            default:
                throw new IllegalArgumentException("Unsupported build tool: " + project.getBuildTool());
        }

        writeStringSafe(templateYamlPath, templateContent);
        items.add(TemplateListItem.from(template));

        TemplateListTable table = new TemplateListTable(items);
        System.out.println(table.getContent());
    }

    private void createSkeletonIfExsists(Path skeletonDir, Path path, Map<String, String> parameters) {
        if (path.toFile().exists()) {
            createSkeleton(skeletonDir, path, parameters);
        }
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

    private Path toSkeletonPath(Path skeletonDir, Path file) {
        return skeletonDir.resolve(getWorkingDirectory().relativize(file));
    }

    private String parameterize(String content, String name, String value) {
        if (value == null) {
            System.err.println("Value for " + name + " is null. Ignoring.");
            return content;
        }
        String placeholder = "\\$\\{\\{ values\\." + name + " \\}\\}";
        return content.contains(value) ? content.replaceAll(Pattern.quote(value), placeholder) : content;
    }
}
