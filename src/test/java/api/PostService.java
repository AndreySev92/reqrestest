package api;

import io.restassured.response.Response;
import java.util.Map;

import static api.BaseSpecifications.getBaseSpec;

public class PostService {

    private static final String POSTS = "/posts";
    private static final String POST_BY_ID = "/posts/{id}";

    public Response createPost(Map<String, Object> postData) {
        return getBaseSpec()
                .body(postData)
                .when()
                .post(POSTS);
    }

    public Response createPostOnInvalidEndpoint(Map<String, Object> postData) {
        return getBaseSpec()
                .body(postData)
                .when()
                .post("/invalid-endpoint-12345");
    }

    public Response getPostsResponse() {
        return getBaseSpec()
                .when()
                .get(POSTS);
    }

    public Response patchUpdatePost(int postId, Map<String, Object> postData) {
        return getBaseSpec()
                .body(postData)
                .when()
                .patch(POST_BY_ID, postId);
    }

    public Response deletePost(int postId) {
        return getBaseSpec()
                .when()
                .delete(POST_BY_ID, postId);
    }
}