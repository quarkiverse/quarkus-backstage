package io.quarkiverse.backstage.cli.template;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.common.dsl.GitActions;
import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.rest.CreateLocationRequest;
import io.quarkiverse.backstage.rest.RefreshEntity;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkiverse.backstage.v1alpha1.Location;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import io.quarkus.devtools.utils.Prompt;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "install", sortOptions = false, mixinStandardHelpOptions = false, header = "Install Backstage Template.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class InstallCommand extends GenerationBaseCommand {

    @Option(names = { "-r", "--remote" }, description = "The git remote to push the template to.")
    String remote = "origin";

    @Option(names = { "-b", "--branch" }, description = "The git branch to push the template to.")
    String branch = "backstage";

    public InstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public void process(EntityList entityList) {
        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        String templateName = Projects.getProjectInfo(project).getOrDefault("artifactId", "my-template");
        Path templatePath = project.getProjectDirPath().resolve(".backstage").resolve("templates").resolve(templateName)
                .resolve("template.yaml");

        Optional<String> url = Git.getUrl(remote, branch, project.getProjectDirPath().relativize(templatePath));
        if (url.isEmpty()) {
            System.out.println("No git remote url found. Template cannot be published. Aborting.");
            return;
        }

        List<TemplateListItem> items = new ArrayList<>();

        if (Prompt.yesOrNo(false, "This operation will trigger a git commit and push. Would you like to proceed? ")
                && commitAndPush()) {
            System.out.println("Backstage Template pushed to the remote repository.");
        } else {
            System.out.println("Backstage Template not pushed to the remote repository. Aborting.");
            return;
        }

        System.out.println("Backstage Template published at: " + url.get());
        final String targetUrl = url.get();

        Optional<Location> existingLocation = getBackstageClient().getAllEntities().stream()
                .filter(e -> e.getKind().equals("Location"))
                .map(e -> (Location) e)
                .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                        || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                .findFirst();

        if (existingLocation.isPresent()) {
            Location l = existingLocation.get();
            String entityRef = "location:" + l.getMetadata().getNamespace().orElse("default") + "/" + l.getMetadata().getName();
            System.out.println("Location already exists: " + entityRef);
            getBackstageClient().refreshEntity(new RefreshEntity(entityRef));
            System.out.println("Refreshed Backstage entities:");
        } else {
            CreateLocationRequest request = new CreateLocationRequest("url", targetUrl);
            getBackstageClient().createLocation(request);
            System.out.println("Installed Backstage entities:");
        }

        for (Entity entity : entityList.getItems()) {
            items.add(TemplateListItem.from(entity));
        }

        TemplateListTable table = new TemplateListTable(items);
        System.out.println(table.getContent());
    }

    private boolean commitAndPush() {
        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        Path rootDir = project.getProjectDirPath();
        Path dotBackstage = rootDir.relativize(rootDir.resolve(".backstage"));
        Path catalogInfoYaml = rootDir.relativize(rootDir.resolve("catalog-info.yaml"));
        GitActions.createTempo()
                .checkoutOrCreateBranch(remote, branch)
                .importFiles(rootDir, dotBackstage, catalogInfoYaml)
                .commit("Generated backstage resources.", dotBackstage, catalogInfoYaml);
        return true;
    }
}
