package io.quarkiverse.backstage.common.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GiteaTest {

    @Test
    void shouldGetHost() {
        String host = Gitea.getHost("http://localhost:3000/");
        assertEquals("localhost", host);
    }

    @Test
    void shouldGetPort() {
        assertEquals(3000, Gitea.getPort("http://localhost:3000"));
        assertEquals(3000, Gitea.getPort("http://localhost:3000/some/path"));
    }
}
