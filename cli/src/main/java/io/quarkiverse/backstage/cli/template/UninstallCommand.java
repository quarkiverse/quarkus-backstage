package io.quarkiverse.backstage.cli.template;

import static io.quarkiverse.backstage.common.utils.Projects.getProjectInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.ws.rs.WebApplicationException;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.model.LocationEntry;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import picocli.CommandLine.Command;

@Command(name = "uninstall", sortOptions = false, mixinStandardHelpOptions = false, header = "Uninstall Backstage Template.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class UninstallCommand extends GenerationBaseCommand {

    public UninstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public void process(EntityList entityList) {
        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        List<TemplateListItem> items = new ArrayList<>();
        String templateName = getProjectInfo(project).getOrDefault("artifactId", "my-template");

        List<Entity> result = getBackstageClient().entities().list("kind=template,metadata.name=" + templateName);
        Map<String, String> locationIdByTarget = new HashMap<>();

        List<LocationEntry> locations = getBackstageClient().locations().list();
        for (LocationEntry item : locations) {
            locationIdByTarget.put(item.getTarget(), item.getId());
        }

        boolean templateFound = false;
        boolean locationFound = false;
        for (Entity entity : result) {
            Entity refreshed = null;
            try {
                refreshed = getBackstageClient().entities()
                        .withKind(entity.getKind().toLowerCase())
                        .withName(entity.getMetadata().getName())
                        .inNamespace(entity.getMetadata().getNamespace().orElse("default"))
                        .get();
                String locationTarget = refreshed.getMetadata().getAnnotations().get("backstage.io/managed-by-origin-location")
                        .replaceAll("url:", "")
                        .replaceAll("file:", "");

                templateFound = true;
                if (locationIdByTarget.containsKey(locationTarget)) {
                    locationFound = true;
                    String locationId = locationIdByTarget.get(locationTarget);
                    getBackstageClient().locations().withId(locationId).delete();
                    Optional<String> uuid = refreshed.getMetadata().getUid();
                    uuid.ifPresent(u -> getBackstageClient().entities().withUID(u).delete());
                    items.add(TemplateListItem.from(entity));
                }
            } catch (WebApplicationException e) {
                if (e.getResponse().getStatus() == 404) {
                    System.out.println(entity.getKind() + " " + entity.getMetadata().getName() + " not installed.");
                } else {
                    throw new RuntimeException(e);
                }
            }

            if (!templateFound) {
                System.out.println("No matching Template named: " + templateName);
            } else if (!locationFound) {
                System.out.println(
                        "No matching Location found for Template: " + templateName + ". Template cannot be uninstalled.");
            } else if (!items.isEmpty()) {
                System.out.println("Uninstalled Backstage templates:");
                TemplateListTable table = new TemplateListTable(items);
                System.out.println(table.getContent());
            } else {
                System.out.println("Did not uninstall any Backstage templates.");
            }
        }
    }
}
