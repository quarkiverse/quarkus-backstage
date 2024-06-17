package io.quarkiverse.backstage.deployment;

import static io.quarkiverse.backstage.deployment.Utils.getRestClientName;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
