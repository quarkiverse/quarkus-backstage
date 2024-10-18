package io.quarkiverse.backstage.cli.template;

import static io.quarkiverse.backstage.common.utils.Projects.getProjectInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.common.template.TemplateGenerator;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
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
        saveCatalogInfo(entityList);

        Map<String, String> parameters = new HashMap<>();
        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        parameters.putAll(getProjectInfo(project));

        String templateName = name.orElse(parameters.getOrDefault("artifactId", "my-template"));
        parameters.put("componentId", templateName);
        TemplateGenerator generator = new TemplateGenerator(project.getProjectDirPath(), templateName,
                namespace.orElse("default"));
        Map<Path, String> templateContent = generator.generate();

        templateContent.forEach((path, content) -> {
            try {
                Files.createDirectories(path.getParent());
                Files.writeString(path, content);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write file: " + path, e);
            }
        });

        Path backstageDir = project.getProjectDirPath().resolve(".backstage");
        Path templatesDir = backstageDir.resolve("templates");
        Path templateDir = templatesDir.resolve(templateName);

        Path templateYamlPath = templateDir.resolve("template.yaml");
        Template template = Serialization.unmarshal(templateContent.get(templateYamlPath), Template.class);

        List<TemplateListItem> items = new ArrayList<>();
        TemplateListTable table = new TemplateListTable(items);
        System.out.println(table.getContent());
    }
}
