package api;

import dto.Post;
import dto.User;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReqresApiTests {

    private UserService userService = new UserService();
    private PostService postService = new PostService();

    @Test
    public void test1_getUsersAndValidateEmail() {
        Response response = userService.getUsersResponse();
        List<User> users = Arrays.asList(response.as(User[].class));

        assertThat(users).isNotEmpty();

        for (User user : users) {
            assertThat(user.getEmail())
                    .as("Email пользователя %s должен содержать @", user.getName())
                    .isNotBlank()
                    .contains("@");
        }
    }

    @Test
    public void test2_createPostSuccess() {
        Map<String, Object> postData = new HashMap<>();
        postData.put("title", "foo");
        postData.put("body", "bar");
        postData.put("userId", 1);

        Response response = postService.createPost(postData);
        Post createdPost = response.as(Post.class);

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(createdPost.getId()).isPositive();
        assertThat(createdPost.getTitle()).isEqualTo("foo");
        assertThat(createdPost.getBody()).isEqualTo("bar");
        assertThat(createdPost.getUserId()).isEqualTo(1);
    }

    @Test
    public void test2_createPostFailure() {
        Response response = postService.createPost(Collections.emptyMap());

        Post createdPost = response.as(Post.class);

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(createdPost.getId()).isPositive();
    }

    @Test
    public void test2_createPostInvalidEndpoint() {
        Map<String, Object> postData = new HashMap<>();
        postData.put("title", "test");

        Response response = postService.createPostOnInvalidEndpoint(postData);

        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    public void test3_postsSortedById() {
        Response response = postService.getPostsResponse();
        List<Post> posts = Arrays.asList(response.as(Post[].class));

        assertThat(posts).isNotEmpty();

        List<Integer> ids = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        assertThat(ids).isSorted();
    }

    @Test
    public void test4_deletePost() {
        Response response = postService.deletePost(2);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    public void test4_updatePostAndValidate() {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("title", "Updated Title");
        updatedData.put("body", "Updated Body");

        Response response = postService.patchUpdatePost(1, updatedData);
        Post updatedPost = response.as(Post.class);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.getBody()).isEqualTo("Updated Body");
    }
}