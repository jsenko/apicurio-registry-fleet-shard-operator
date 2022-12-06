package io.apicurio.my.ext.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class TestResourceTest {

    @Test
    void testCRScoped() {
        assertEquals("", get("a", "animal"));
        put("a", "animal", "dog");
        assertEquals("dog", get("a", "animal"));

        assertEquals("", get("b", "animal"));
        put("b", "animal", "cat");
        assertEquals("cat", get("b", "animal"));

        assertEquals("dog", get("a", "animal"));
        remove("a", "animal");
        assertEquals("", get("a", "animal"));

        assertEquals("cat", get("b", "animal"));
    }

    private String get(String scope, String key) {
        return given()
                .log().all()
                .when().get("/test/{scope}/{key}", scope, key)
                .then()
                //.statusCode(200)
                .extract().asString();
    }

    private void put(String scope, String key, String value) {
        given()
                .log().all()
                .when().body(value).post("/test/{scope}/{key}", scope, key)
                .then().statusCode(204);
    }

    private void remove(String scope, String key) {
        given()
                .log().all()
                .when().delete("/test/{scope}/{key}", scope, key)
                .then().statusCode(204);
    }
}
