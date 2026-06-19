package api;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseSpecifications {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    protected RequestSpecification getBaseSpec() {
        return given()
                .baseUri(BASE_URL)
                .contentType(io.restassured.http.ContentType.JSON)
                .accept(io.restassured.http.ContentType.JSON);
    }
}