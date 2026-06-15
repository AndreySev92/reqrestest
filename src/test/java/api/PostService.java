package api;

import dto.Post;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PostService extends BaseTest {

    private static final String POSTS = "/posts";
    private static final String POST_BY_ID = "/posts/{id}";

    public Response createPost(Map<String, Object> postData) {
        return getBaseSpec()
                .body(postData)
                .when()
                .post(POSTS);
    }

    public Response getPostsResponse() {
        return getBaseSpec()
                .when()
                .get(POSTS);
    }

    // Возвращает список постов как DTO
    public List<Post> getPostsAsDto() {
        return Arrays.asList(getBaseSpec()
                .when()
                .get(POSTS)
                .then()
                .extract()
                .as(Post[].class));
    }

    public Post getPostByIdAsDto(int postId) {
        return getBaseSpec()
                .when()
                .get(POST_BY_ID, postId)
                .then()
                .extract()
                .as(Post.class);
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