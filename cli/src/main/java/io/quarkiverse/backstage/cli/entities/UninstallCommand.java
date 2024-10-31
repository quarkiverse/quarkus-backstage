package io.quarkiverse.backstage.cli.entities;

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
import picocli.CommandLine.Command;

@Command(name = "uninstall", sortOptions = false, mixinStandardHelpOptions = false, header = "Uninstall Backstage Application.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class UninstallCommand extends GenerationBaseCommand {

    public UninstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public void process(EntityList entityList) {
        if (entityList.getItems().isEmpty()) {
            System.out.println("No Backstage entities detected.");
            return;
        }
        List<EntityListItem> items = new ArrayList<>();
        Map<String, String> locationIdByTarget = new HashMap<>();

        List<LocationEntry> locations = getBackstageClient().locations().list();
        for (LocationEntry item : locations) {
            locationIdByTarget.put(item.getTarget(), item.getId());
        }

        System.out.println();
        for (Entity entity : entityList.getItems()) {
            Entity refreshed = null;
            try {
                refreshed = getBackstageClient().entities().withKind(entity.getKind())
                        .withName(entity.getMetadata().getName())
                        .inNamespace(entity.getMetadata().getNamespace().orElse("default"))
                        .get();

                String locationTarget = refreshed.getMetadata().getAnnotations().get("backstage.io/managed-by-origin-location")
                        .replaceAll("url:", "");

                if (locationIdByTarget.containsKey(locationTarget)) {
                    String locationId = locationIdByTarget.get(locationTarget);
                    getBackstageClient().locations().withId(locationId).delete();
                }

                Optional<String> uuid = refreshed.getMetadata().getUid();
                uuid.ifPresent(u -> getBackstageClient().entities().withUID(u).delete());
                items.add(EntityListItem.from(entity));
            } catch (WebApplicationException e) {
                e.printStackTrace();
                if (e.getResponse().getStatus() == 404) {
                    System.out.println(entity.getKind() + " " + entity.getMetadata().getName() + " not installed.");
                } else {
                    throw new RuntimeException(e);
                }
            }

            if (!items.isEmpty()) {
                System.out.println("Uninstalled Backstage entities:");
                EntityListTable table = new EntityListTable(items);
                System.out.println(table.getContent());
            } else {
                System.out.println("Did not uninstall any Backstage entities.");
            }
        }
    }
}
