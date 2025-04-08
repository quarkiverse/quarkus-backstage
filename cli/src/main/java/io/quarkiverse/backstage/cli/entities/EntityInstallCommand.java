package io.quarkiverse.backstage.cli.entities;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.dsl.GitActions;
import io.quarkiverse.backstage.common.handlers.GetBackstageEntitiesHandler;
import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.utils.Strings;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkiverse.backstage.v1alpha1.Location;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import io.quarkus.devtools.utils.Prompt;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "install", sortOptions = false, mixinStandardHelpOptions = false, header = "Install Backstage Entities.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class EntityInstallCommand extends GenerationBaseCommand<EntityList> {

    @Option(names = { "-r", "--remote" }, description = "The git remote to push the template to. Defaults to origin.")
    String remote = "origin";

    @Option(names = { "-b", "--branch" }, description = "The git branch to push the template to. Defaults to backstage.")
    String branch = "backstage";

    @Option(names = { "-y",
            "--yes" }, description = "Option to automatically select 'yes' in all prompts. This is usefull when running the command in non-interactive mode.")
    boolean yes;

    public EntityInstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public String getHandlerName() {
        return GetBackstageEntitiesHandler.class.getName();
    }

    @Override
    public String[] getRequiredBuildItems() {
        return new String[] { EntityListBuildItem.class.getName() };
    }

    @Override
    public void process(EntityList entityList, Path... additionalFiles) {
        if (entityList.getItems().isEmpty()) {
            System.out.println("No Backstage entities detected.");
            return;
        }

        final QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        List<EntityListItem> items = new ArrayList<>();

        String content = Serialization.asYaml(entityList);
        Path catalogPath = project.getProjectDirPath().resolve("catalog-info.yaml");
        writeStringSafe(catalogPath, content);

        if (Strings.isNullOrEmpty(remote)) {
            remote = "origin";
        }

        if (Strings.isNullOrEmpty(branch)) {
            remote = "backstage";
        }

        Optional<String> url = Git.getUrl(remote, branch, project.getProjectDirPath().relativize(catalogPath));
        if (url.isEmpty()) {
            System.out.println("No git remote url found. Template cannot be published. Aborting.");
            return;
        }

        if ((yes || Prompt.yesOrNo(false, "This operation will trigger a git commit and push. Would you like to proceed? "))
                && commitAndPush(additionalFiles)) {
            System.out.println("Backstage entities pushed to the remote repository.");
        } else {
            System.out.println("Backstage entities not pushed to the remote repository. Aborting.");
            return;
        }

        if (url.isPresent()) {
            System.out.println("Backstage entities published at: " + url.get());
        } else {
            System.out.println("Backstage entities not published. Aborting.");
            return;
        }

        final String targetUrl = url.get();

        Optional<Location> existingLocation = getBackstageClient().entities().list().stream()
                .filter(e -> e.getKind().equals("Location"))
                .map(e -> (Location) e)
                .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                        || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                .findFirst();

        if (existingLocation.isPresent()) {
            Location l = existingLocation.get();
            getBackstageClient().entities().withKind("location").withName(l.getMetadata().getName()).inNamespace("default")
                    .refresh();
            System.out.println("Refreshed Backstage entities:");
        } else {
            getBackstageClient().locations().createFromUrl(targetUrl);
            System.out.println("Installed Backstage entities:");
        }

        for (Entity entity : entityList.getItems()) {
            items.add(EntityListItem.from(entity));
        }

        EntityListTable table = new EntityListTable(items);
        System.out.println(table.getContent());
    }

    private boolean commitAndPush(Path... additionalFiles) {
        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        Path rootDir = project.getProjectDirPath();
        Path catalogInfoYaml = rootDir.relativize(rootDir.resolve("catalog-info.yaml"));
        List<Path> toCommit = new ArrayList<>();
        toCommit.add(catalogInfoYaml);
        for (Path additionalFile : additionalFiles) {
            toCommit.add(additionalFile);
        }

        String remoteUrl = Git.getRemoteUrl(rootDir, remote)
                .orElseThrow(() -> new IllegalStateException("No remote url found."));
        GitActions.createTempo()
                .addRemote(remote, remoteUrl)
                .checkoutOrCreateBranch(remote, branch)
                .importFiles(rootDir, toCommit.toArray(Path[]::new))
                .commit("Generated backstage resources.", toCommit.toArray(Path[]::new))
                .push(remote, branch);
        return true;
    }
}
