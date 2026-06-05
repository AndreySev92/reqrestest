package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReqresApiTests {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.requestSpecification = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json");
    }

    @Test
    public void test1_getUsersAndValidateData() {
        Response response = given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<String> emails = response.jsonPath().getList("email");
        for (String email : emails) {
            assertThat(email, notNullValue());
            assertThat(email, containsString("@"));
        }

        List<String> names = response.jsonPath().getList("name");
        for (String name : names) {
            assertThat(name, not(is(emptyOrNullString())));
        }

        List<Integer> ids = response.jsonPath().getList("id");
        for (Integer id : ids) {
            assertThat(id, notNullValue());
        }
    }

    @Test
    public void test2_1_createPostSuccess() {
        Map<String, Object> post = new HashMap<>();
        post.put("title", "foo");
        post.put("body", "bar");
        post.put("userId", 1);

        given()
                .body(post)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("foo"))
                .body("body", equalTo("bar"))
                .body("userId", equalTo(1));
    }

    @Test
    public void test2_1_createPostFailure() {
        given()
                .body("{}")
                .when()
                .post("/posts")
                .then()
                .statusCode(201);
    }

    @Test
    public void test2_1_createPostInvalidEndpoint() {
        Map<String, Object> post = new HashMap<>();
        post.put("title", "test");

        given()
                .body(post)
                .when()
                .post("/invalid-endpoint-12345")
                .then()
                .statusCode(404);
    }

    @Test
    public void test3_resourcesSortedById() {
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Integer> ids = response.jsonPath().getList("id");

        List<Integer> sortedIds = new ArrayList<>(ids);
        sortedIds.sort(Integer::compareTo);

        assertThat(ids, equalTo(sortedIds));
    }

    @Test
    public void test4_1_deletePost() {
        given()
                .when()
                .delete("/posts/2")
                .then()
                .statusCode(200);
    }

    @Test
    public void test4_2_updatePostAndValidate() {
        Map<String, Object> updatedPost = new HashMap<>();
        updatedPost.put("title", "Updated Title");
        updatedPost.put("body", "Updated Body");

        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Response updateResponse = given()
                .body(updatedPost)
                .when()
                .patch("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String updatedTitle = updateResponse.jsonPath().getString("title");
        String updatedBody = updateResponse.jsonPath().getString("body");

        assertThat(updatedTitle, equalTo("Updated Title"));
        assertThat(updatedBody, equalTo("Updated Body"));

        System.out.println("Пост успешно обновлен в " + currentDateTime);
    }

    @Test
    public void testEmailValidation() {
        Response response = given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<String> emails = response.jsonPath().getList("email");

        for (String email : emails) {
            assertThat(email, containsString("@"));
            assertThat(email, not(is(emptyOrNullString())));
        }
    }
}