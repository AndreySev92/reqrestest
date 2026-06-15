package api;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    protected static RequestSpecification getBaseSpec(){
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }


}
