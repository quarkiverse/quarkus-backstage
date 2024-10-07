package io.quarkiverse.backstage.cli.entities;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.quarkiverse.backstage.cli.utils.Git;
import io.quarkiverse.backstage.cli.utils.Github;
import io.quarkiverse.backstage.deployment.utils.Serialization;
import io.quarkiverse.backstage.rest.CreateLocationRequest;
import io.quarkiverse.backstage.rest.RefreshEntity;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkiverse.backstage.v1alpha1.Location;
import io.quarkus.devtools.utils.Prompt;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "install", sortOptions = false, mixinStandardHelpOptions = false, header = "Install Backstage Entities.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class InstallCommand extends GenerationBaseCommand {

    @Option(names = { "--github-token" }, description = "The github token to use for publishing components to gist.")
    Optional<String> githubToken = Optional.empty();

    public InstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    void process(EntityList entityList) {
        if (entityList.getItems().isEmpty()) {
            System.out.println("No Backstage entities detected.");
            return;
        }

        List<EntityListItem> items = new ArrayList<>();

        String content = Serialization.asYaml(entityList);
        Path catalogPath = getWorkingDirectory().resolve("catalog-info.yaml");
        writeStringSafe(catalogPath, content);

        Optional<String> url = Optional.empty();
        Optional<String> githubToken = Github.getToken();

        if (githubToken.isPresent()) {
            url = Github.createOrUpdatePrivateGist(githubToken, "Backstage entities", "catalog-info.yaml", content);
        } else if (Prompt.yesOrNo(false, "This operation will trigger a git commit and push. Would you like to proceed? ")
                && commit() && push()) {
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

        Optional<Location> existingLocation = backstageClient.getAllEntities().stream()
                .filter(e -> e.getKind().equals("Location"))
                .map(e -> (Location) e)
                .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                        || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                .findFirst();

        if (existingLocation.isPresent()) {
            Location l = existingLocation.get();
            System.out.println("Location already exists: " + entityRef);
            backstageClient.refreshEntity(new RefreshEntity(entityRef));
            System.out.println("Refreshed Backstage entities:");
        } else {
            CreateLocationRequest request = new CreateLocationRequest("url", targetUrl);
            backstageClient.createLocation(request);
            System.out.println("Installed Backstage entities:");
        }

        for (Entity entity : entityList.getItems()) {
            items.add(EntityListItem.from(entity));
        }

        EntityListTable table = new EntityListTable(items);
        System.out.println(table.getContent());
    }

    private boolean commit() {
        Path catalogPath = getWorkingDirectory().resolve("catalog-info.yaml");
        return Git.commit("Backstage entities generated.", catalogPath);
    }

    private boolean push() {
        Git.push("origin", "main");
        return true;
    }
}
