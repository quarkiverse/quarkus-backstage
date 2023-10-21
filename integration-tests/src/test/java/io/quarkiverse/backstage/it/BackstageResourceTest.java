package io.quarkiverse.backstage.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BackstageResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/backstage")
                .then()
                .statusCode(200)
                .body(is("Hello backstage"));
    }
}
