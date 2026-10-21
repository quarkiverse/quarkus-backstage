package io.quarkiverse.backstage.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.BackstageEntityNotFoundException;
import io.quarkiverse.backstage.client.model.CreateLocationResponse;
import io.quarkiverse.backstage.client.model.LocationEntry;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.Location;

/**
 * Wraps the logic of installing / uninstalling Backstage locations, entities and templates,
 * so that it can be shared between the CLI, the Dev UI and the Dev Services.
 * Publishing the resources (e.g. git push) is the responsibility of the caller.
 */
public class BackstageInstallService {

    private static final String MANAGED_BY_ORIGIN_LOCATION = "backstage.io/managed-by-origin-location";

    public enum UninstallStatus {
        UNINSTALLED,
        NOT_INSTALLED,
        LOCATION_NOT_FOUND
    }

    private final BackstageClient backstageClient;

    public BackstageInstallService(BackstageClient backstageClient) {
        this.backstageClient = backstageClient;
    }

    public BackstageClient getBackstageClient() {
        return backstageClient;
    }

    /**
     * Find the {@link Location} entity that targets the specified url.
     */
    public Optional<Location> findLocation(String targetUrl) {
        return backstageClient.entities().list().stream()
                .filter(e -> e.getKind().equals("Location"))
                .map(e -> (Location) e)
                .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                        || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                .findFirst();
    }

    /**
     * Refresh the location that targets the specified url, or create it if missing.
     *
     * @return the response of the location creation, or empty if an existing location was refreshed.
     */
    public Optional<CreateLocationResponse> installOrRefresh(String targetUrl) {
        return installOrRefresh(targetUrl, false);
    }

    /**
     * Refresh the location that targets the specified url, or create it if missing.
     *
     * @param dryRun when true, a dry run is performed before creating the location and fails if no entities are found.
     * @return the response of the location creation, or empty if an existing location was refreshed.
     */
    public Optional<CreateLocationResponse> installOrRefresh(String targetUrl, boolean dryRun) {
        Optional<Location> existingLocation = findLocation(targetUrl);
        if (existingLocation.isPresent()) {
            Location l = existingLocation.get();
            backstageClient.entities().withKind("location").withName(l.getMetadata().getName())
                    .inNamespace(l.getMetadata().getNamespace().orElse("default")).refresh();
            return Optional.empty();
        }

        if (dryRun) {
            CreateLocationResponse response = backstageClient.locations().dryRun().createFromUrl(targetUrl);
            if (response.getEntities().isEmpty()) {
                throw new RuntimeException("Failed to dry-run location");
            }
        }
        return Optional.of(backstageClient.locations().createFromUrl(targetUrl));
    }

    /**
     * Create a location from a url (http, https, git) or a file.
     */
    public CreateLocationResponse installLocation(String target) {
        if (target.matches("^(http|https|git)://.*$")) {
            return backstageClient.locations().createFromUrl(target);
        }
        return backstageClient.locations().createFromFile(target);
    }

    /**
     * Find the id of the location that targets the specified target.
     */
    public Optional<String> findLocationId(String target) {
        return Optional.ofNullable(getLocationIdByTarget().get(target));
    }

    public boolean uninstallLocation(String locationId) {
        return backstageClient.locations().withId(locationId).delete();
    }

    /**
     * Uninstall the specified entities along with the locations that manage them.
     * Entities are deleted even when no matching location is found.
     *
     * @return the status of each entity that was processed.
     */
    public Map<Entity, UninstallStatus> uninstallEntities(List<Entity> entities) {
        Map<Entity, UninstallStatus> result = new LinkedHashMap<>();
        Map<String, String> locationIdByTarget = getLocationIdByTarget();
        List<String> uninstalledLocations = new ArrayList<>();

        for (Entity entity : entities) {
            try {
                Entity refreshed = getInstalled(entity);
                String locationTarget = getLocationTarget(refreshed);

                if (locationIdByTarget.containsKey(locationTarget)) {
                    String locationId = locationIdByTarget.get(locationTarget);
                    if (!uninstalledLocations.contains(locationId)) {
                        // Do not delete the same location twice
                        uninstallLocation(locationId);
                        uninstalledLocations.add(locationId);
                    }
                }

                refreshed.getMetadata().getUid().ifPresent(u -> backstageClient.entities().withUID(u).delete());
                result.put(entity, UninstallStatus.UNINSTALLED);
            } catch (WebApplicationException e) {
                if (e.getResponse().getStatus() == 404) {
                    result.put(entity, UninstallStatus.NOT_INSTALLED);
                } else if (uninstalledLocations.isEmpty()) {
                    // Only ignore the exception if the location is already deleted
                    throw new RuntimeException(e);
                }
            } catch (BackstageEntityNotFoundException e) {
                result.put(entity, UninstallStatus.NOT_INSTALLED);
            }
        }
        return result;
    }

    /**
     * Uninstall a template along with the location that manages it.
     * The template is not deleted when no matching location is found.
     */
    public UninstallStatus uninstallTemplate(Entity template) {
        Map<String, String> locationIdByTarget = getLocationIdByTarget();
        try {
            Entity refreshed = getInstalled(template);
            String locationTarget = getLocationTarget(refreshed);

            String locationId = locationIdByTarget.get(locationTarget);
            if (locationId == null) {
                return UninstallStatus.LOCATION_NOT_FOUND;
            }

            uninstallLocation(locationId);
            refreshed.getMetadata().getUid().ifPresent(u -> backstageClient.entities().withUID(u).delete());
            return UninstallStatus.UNINSTALLED;
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                return UninstallStatus.NOT_INSTALLED;
            }
            throw new RuntimeException(e);
        } catch (BackstageEntityNotFoundException e) {
            return UninstallStatus.NOT_INSTALLED;
        }
    }

    private Entity getInstalled(Entity entity) {
        return backstageClient.entities()
                .withKind(entity.getKind().toLowerCase())
                .withName(entity.getMetadata().getName())
                .inNamespace(entity.getMetadata().getNamespace().orElse("default"))
                .get();
    }

    private static String getLocationTarget(Entity entity) {
        return entity.getMetadata().getAnnotations().get(MANAGED_BY_ORIGIN_LOCATION)
                .replaceAll("url:", "")
                .replaceAll("file:", "");
    }

    private Map<String, String> getLocationIdByTarget() {
        return backstageClient.locations().list().stream()
                .collect(Collectors.toMap(LocationEntry::getTarget, LocationEntry::getId, (a, b) -> a));
    }
}
