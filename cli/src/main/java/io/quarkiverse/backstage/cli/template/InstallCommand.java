package io.quarkiverse.backstage.cli.template;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.quarkiverse.backstage.cli.common.BackstageClientAwareCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.dsl.GitActions;
import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.common.utils.Templates;
import io.quarkiverse.backstage.v1alpha1.Location;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import io.quarkus.devtools.utils.Prompt;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;

@Command(name = "install", sortOptions = false, mixinStandardHelpOptions = false, header = "Install Backstage Template.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class InstallCommand extends BackstageClientAwareCommand {

    @Option(names = { "--dev-template" }, description = "Flag for also installing a dev template. Default is false.")
    boolean generateDevTemplate;

    @Option(names = { "-r", "--remote" }, description = "The git remote to push the template to.")
    String remote = "origin";

    @Option(names = { "-b", "--branch" }, description = "The git branch to push the template to.")
    String branch = "backstage";

    public InstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public Integer call() throws Exception {
        QuarkusProject project = QuarkusProjectHelper.getProject(Projects.getProjectRoot());
        List<TemplateListItem> items = new ArrayList<>();

        Path backstageDir = project.getProjectDirPath().resolve(".backstage");
        if (!backstageDir.toFile().exists()) {
            System.out.println("No .backstage dir found. Have you run generate ?");
            return ExitCode.USAGE;
        }

        Path templatesDir = backstageDir.resolve("templates");
        if (!templatesDir.toFile().exists()) {
            System.out.println("No backstage templates found. Aborting.");
            return ExitCode.USAGE;
        }

        Files.list(templatesDir).forEach(templateDir -> {
            Path templatePath = templateDir.resolve("template.yaml");

            Optional<String> url = Git.getUrl(remote, branch, project.getProjectDirPath().relativize(templatePath));
            if (url.isEmpty()) {
                System.out.println("No git remote url found. Template cannot be published. Aborting.");
                return;
            }

            if (Prompt.yesOrNo(false, "This operation will trigger a git commit and push. Would you like to proceed? ")
                    && commitAndPush()) {
                System.out.println("Backstage Template pushed to the remote repository.");
            } else {
                System.out.println("Backstage Template not pushed to the remote repository. Aborting.");
                return;
            }

            System.out.println("Backstage Template published at: " + url.get());
            final String targetUrl = url.get();

            Optional<Location> existingLocation = getBackstageClient().entities().list().stream()
                    .filter(e -> e.getKind().equals("Location"))
                    .map(e -> (Location) e)
                    .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                            || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                    .findFirst();

            if (existingLocation.isPresent()) {
                Location l = existingLocation.get();
                getBackstageClient().entities().withKind("location").withName(l.getMetadata().getName())
                        .inNamespace(l.getMetadata().getNamespace().orElse("default")).refresh();
                System.out.println("Refreshed Backstage entities:");
            } else {
                getBackstageClient().locations().createFromUrl(targetUrl);
                System.out.println("Installed Backstage entities:");
            }

            items.add(TemplateListItem.from(Templates.createTemplateEntity(templateDir)));
        });

        TemplateListTable table = new TemplateListTable(items);
        System.out.println(table.getContent());
        return ExitCode.OK;
    }

    private boolean commitAndPush(Path... additionalFiles) {
        Path rootDir = Projects.getProjectRoot();
        String remoteUrl = Git.getRemoteUrl(rootDir, remote)
                .orElseThrow(() -> new IllegalStateException("No remote url found."));
        Path dotBackstage = rootDir.relativize(rootDir.resolve(".backstage"));
        Path catalogInfoYaml = rootDir.relativize(rootDir.resolve("catalog-info.yaml"));

        List<Path> toCommit = new ArrayList<>();
        toCommit.add(catalogInfoYaml);
        toCommit.add(dotBackstage);
        for (Path additionalFile : additionalFiles) {
            toCommit.add(additionalFile);
        }

        GitActions.createTempo()
                .addRemote(remote, remoteUrl)
                .createBranch(branch)
                .importFiles(rootDir, toCommit.toArray(Path[]::new))
                .commit("Generated backstage resources.", toCommit.toArray(Path[]::new))
                .push(remote, branch);
        return true;
    }
}
