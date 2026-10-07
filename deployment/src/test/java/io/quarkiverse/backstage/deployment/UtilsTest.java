package io.quarkiverse.backstage.deployment;

import static io.quarkiverse.backstage.deployment.Utils.getRestClientDependencyName;
import static io.quarkiverse.backstage.deployment.Utils.getRestClientName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    public void testServiceNameExtraction() {
        assertEquals(getRestClientName("quarkus.rest-client.my-service.url"), Optional.of("my-service"));
        assertEquals(getRestClientName("quarkus.rest-client.other-service.url"), Optional.of("other-service"));
        assertEquals(getRestClientName("quarkus.rest-client.yet-another-service.url"), Optional.of("yet-another-service"));
        assertEquals(getRestClientName("quarkus.rest-client..url"), Optional.empty());
    }

    @Test
    public void testDependencyNameUsesNameProperty() {
        Map<String, String> config = Map.of(
                "quarkus.rest-client.blub.name", "blub-api",
                "quarkus.rest-client.blank.name", " ");
        assertEquals(getRestClientDependencyName("quarkus.rest-client.blub.url",
                k -> Optional.ofNullable(config.get(k))), Optional.of("blub-api"));
        assertEquals(getRestClientDependencyName("quarkus.rest-client.other.url",
                k -> Optional.ofNullable(config.get(k))), Optional.of("other"));
        assertEquals(getRestClientDependencyName("quarkus.rest-client.blank.url",
                k -> Optional.ofNullable(config.get(k))), Optional.of("blank"));
        assertEquals(getRestClientDependencyName("quarkus.rest-client.blub.name",
                k -> Optional.ofNullable(config.get(k))), Optional.empty());
    }
}
