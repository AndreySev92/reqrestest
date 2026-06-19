package api;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static api.BaseSpecifications.getBaseSpec;

@Slf4j
public class PostService {

    private static final String POSTS = "/posts";
    private static final String POST_BY_ID = "/posts/{id}";

    public Response createPost(Map<String, Object> postData) {
        log.info("POST запрос к {} с данными: {}", POSTS, postData);
        return getBaseSpec()
                .body(postData)
                .when()
                .post(POSTS);
    }

    public Response createPostOnInvalidEndpoint(Map<String, Object> postData) {
        String invalidEndpoint = "/invalid-endpoint-12345";
        log.info("POST запрос к {} (невалидный эндпоинт)", invalidEndpoint);
        return getBaseSpec()
                .body(postData)
                .when()
                .post(invalidEndpoint);
    }

    public Response getPostsResponse() {
        log.info("GET запрос к {}", POSTS);
        return getBaseSpec()
                .when()
                .get(POSTS);
    }

    public Response patchUpdatePost(int postId, Map<String, Object> postData) {
        log.info("PATCH запрос к {} с данными: {}", POST_BY_ID.replace("{id}", String.valueOf(postId)), postData);
        return getBaseSpec()
                .body(postData)
                .when()
                .patch(POST_BY_ID, postId);
    }

    public Response deletePost(int postId) {
        log.info("DELETE запрос к {}", POST_BY_ID.replace("{id}", String.valueOf(postId)));
        return getBaseSpec()
                .when()
                .delete(POST_BY_ID, postId);
    }
}