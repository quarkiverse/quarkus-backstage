package io.quarkiverse.backstage.cli.entities;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.ws.rs.WebApplicationException;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.BackstageEntityNotFoundException;
import io.quarkiverse.backstage.client.model.LocationEntry;
import io.quarkiverse.backstage.common.handlers.GetBackstageEntitiesHandler;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "uninstall", sortOptions = false, mixinStandardHelpOptions = false, header = "Uninstall Backstage Application.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class UninstallCommand extends GenerationBaseCommand<EntityList> {

    @Parameters(index = "0", arity = "0..1", description = "The name of the template.")
    private Optional<String> name = Optional.empty();

    public UninstallCommand(BackstageClient backstageClient) {
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
        List<EntityListItem> items = new ArrayList<>();
        Map<String, String> locationIdByTarget = new HashMap<>();

        List<LocationEntry> locations = getBackstageClient().locations().list();
        for (LocationEntry item : locations) {
            locationIdByTarget.put(item.getTarget(), item.getId());
        }

        List<Entity> toDelete = new ArrayList<>();
        name.ifPresentOrElse(name -> {
            StringBuilder sb = new StringBuilder();
            sb.append("metadata.name=").append(name);
            namespace.ifPresent(namespace -> sb.append(",metadata.namespace=").append(namespace));
            String filter = sb.toString();
            getBackstageClient().entities().list(filter).forEach(toDelete::add);
        }, () -> toDelete.addAll(entityList.getItems()));

        System.out.println();
        List<String> uninstalledLocations = new ArrayList<>();
        for (Entity entity : toDelete) {
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
                    if (!uninstalledLocations.contains(locationIdByTarget.get(locationTarget))) {
                        // Do not delete the same location twice
                        getBackstageClient().locations().withId(locationId).delete();
                        uninstalledLocations.add(locationId);
                    }
                }

                Optional<String> uuid = refreshed.getMetadata().getUid();
                uuid.ifPresent(u -> getBackstageClient().entities().withUID(u).delete());
                items.add(EntityListItem.from(entity));
            } catch (WebApplicationException e) {
                e.printStackTrace();
                if (e.getResponse().getStatus() == 404) {
                    System.out.println(entity.getKind() + " " + entity.getMetadata().getName() + " not installed.");
                } else if (!uninstalledLocations.isEmpty()) {
                    // Do not throw an exception if the location is already deleted
                } else {
                    throw new RuntimeException(e);
                }
            } catch (BackstageEntityNotFoundException e) {
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
