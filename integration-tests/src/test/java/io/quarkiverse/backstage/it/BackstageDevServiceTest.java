package io.quarkiverse.backstage.it;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.model.LocationEntry;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BackstageDevServiceTest {

    @Inject
    BackstageClient backstageClient;

    @Test
    public void shouldInjectClient() {
        assertNotNull(backstageClient);
    }

    @Test
    public void shouldListEntities() {
        List<Entity> entities = backstageClient.entities().list();
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void shouldListLocations() {
        List<LocationEntry> locations = backstageClient.locations().list();
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
    }

    @Test
    public void shouldFindComponent() {
        List<Entity> entities = backstageClient.entities().list();
        assertTrue(entities.stream().anyMatch(e -> e.getKind().equalsIgnoreCase("component")));
    }

    @Test
    public void shouldFindComponentLocation() {
        List<Entity> entities = backstageClient.entities().list();
        Optional<Entity> entitty = entities.stream().filter(e -> e.getKind().equalsIgnoreCase("component")).findFirst();
        assertTrue(entitty.isPresent());
        entitty.ifPresent(e -> {
            LocationEntry location = backstageClient.locations()
                    .withKind("component")
                    .withName(e.getMetadata().getName())
                    .inNamespace(e.getMetadata().getNamespace().orElse("default"))
                    .get();

            assertNotNull(location);
        });
    }
}
