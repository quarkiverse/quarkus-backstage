package io.quarkiverse.backstage.cli.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.ws.rs.WebApplicationException;

import io.quarkiverse.backstage.rest.LocationItem;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import picocli.CommandLine.Command;

@Command(name = "uninstall", sortOptions = false, mixinStandardHelpOptions = false, header = "Uninstall ArgoCD Application.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class UninstallCommand extends GenerationBaseCommand {

    public UninstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    void process(EntityList entityList) {
        if (entityList.getItems().isEmpty()) {
            System.out.println("No Backstage entities detected.");
            return;
        }
        List<EntityListItem> items = new ArrayList<>();
        Map<String, String> locationIdByTarget = new HashMap<>();
        List<Entity> allEntities = backstageClient.getAllEntities();

        List<LocationItem> locations = backstageClient.getLocations();
        for (LocationItem item : locations) {
            locationIdByTarget.put(item.getData().getTarget(), item.getData().getId());
        }

        System.out.println();
        for (Entity entity : entityList.getItems()) {
            Entity refreshed = null;
            try {
                refreshed = backstageClient.getEntity(entity.getKind().toLowerCase(),
                        entity.getMetadata().getNamespace().orElse("default"), entity.getMetadata().getName());
                String locationTarget = refreshed.getMetadata().getAnnotations().get("backstage.io/managed-by-location")
                        .replaceAll("url:", "");

                if (locationIdByTarget.containsKey(locationTarget)) {
                    String locationId = locationIdByTarget.get(locationTarget);
                    backstageClient.deleteLocation(locationId);
                }

                Optional<String> uuid = refreshed.getMetadata().getUid();
                uuid.ifPresent(backstageClient::deleteEntity);
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
