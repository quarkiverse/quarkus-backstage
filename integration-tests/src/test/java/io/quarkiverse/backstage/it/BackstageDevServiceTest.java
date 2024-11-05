package io.quarkiverse.backstage.it;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.model.LocationEntry;
import io.quarkiverse.backstage.client.model.ScaffolderEvent;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
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

    @Test
    public void shouldFindTemplates() {
        List<Entity> entities = backstageClient.entities().list();
        List<Template> templates = entities.stream()
                .filter(e -> e instanceof Template)
                .map(e -> (Template) e)
                .collect(Collectors.toList());
        assertTrue(templates.size() >= 2);
    }

    @Test
    public void shouldInstantiateTemplate() throws InterruptedException {
        String taskId = backstageClient.templates().withName("quarkus-backstage-integration-tests-dev").instantiate(Map.of());
        assertNotNull(taskId);
        List<ScaffolderEvent> events = backstageClient.events().forTask(taskId).waitingUntilCompletion().get();
        events.forEach(e -> {
            System.out.println(e.getCreatedAt() + " " + e.getType() + " " + e.getBody().getMessage());
        });
    }
}
