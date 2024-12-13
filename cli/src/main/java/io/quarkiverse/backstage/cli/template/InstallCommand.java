package io.quarkiverse.backstage.cli.template;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.dsl.GitActions;
import io.quarkiverse.backstage.common.handlers.GetBackstageTemplatesHandler;
import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.spi.DevTemplateBuildItem;
import io.quarkiverse.backstage.spi.TemplateBuildItem;
import io.quarkiverse.backstage.v1alpha1.Location;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import io.quarkus.devtools.utils.Prompt;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "install", sortOptions = false, mixinStandardHelpOptions = false, header = "Install Backstage Template.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class InstallCommand extends GenerationBaseCommand<List<TemplateBuildItem>> {

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
    public String getHandlerName() {
        return GetBackstageTemplatesHandler.class.getName();
    }

    @Override
    public String[] getRequiredBuildItems() {
        if (generateDevTemplate) {
            return new String[] { TemplateBuildItem.class.getName(), DevTemplateBuildItem.class.getName() };
        }
        return new String[] { TemplateBuildItem.class.getName() };
    }

    @Override
    public Properties getBuildSystemProperties() {
        Properties properties = super.getBuildSystemProperties();
        if (generateDevTemplate) {
            properties.put("quarkus.backstage.dev-template.generation.enabled", "true");
        }
        return properties;
    }

    @Override
    public void process(List<TemplateBuildItem> templateBuildItems, Path... additionalFiles) {
        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        List<TemplateListItem> items = new ArrayList<>();

        for (TemplateBuildItem templateBuildItem : templateBuildItems) {
            String templateName = Projects.getProjectInfo(project).getOrDefault("artifactId", "my-template");
            Path templatePath = project.getProjectDirPath().resolve(".backstage").resolve("templates").resolve(templateName)
                    .resolve("template.yaml");

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

            items.add(TemplateListItem.from(templateBuildItem.getTemplate()));
        }

        TemplateListTable table = new TemplateListTable(items);
        System.out.println(table.getContent());
    }

    private boolean commitAndPush(Path... additionalFiles) {
        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        Path rootDir = project.getProjectDirPath();
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
