package api;

import dto.Post;
import dto.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("API тесты для JSONPlaceholder")
public class ReqresApiTests {

    private static UserService userService;
    private static PostService postService;

    @BeforeAll
    public static void setUp() {
        userService = new UserService();
        postService = new PostService();
    }

    @Test
    @DisplayName("Тест 1: Получить пользователей и проверить email")
    public void test1_getUsersAndValidateEmail() {
        Response response = userService.getUsersResponse();
        List<User> users = Arrays.asList(response.as(User[].class));

        assertThat(users)
                .as("Список пользователей не должен быть пустым")
                .isNotEmpty();

        for (User user : users) {
            assertThat(user.getEmail())
                    .as("Email пользователя %s должен содержать @", user.getName())
                    .isNotBlank()
                    .contains("@");
        }
    }

    @Test
    @DisplayName("Тест 2.1: Успешное создание поста")
    public void test2_createPostSuccess() {
        Map<String, Object> postData = new HashMap<>();
        postData.put("title", "foo");
        postData.put("body", "bar");
        postData.put("userId", 1);

        Response response = postService.createPost(postData);
        Post createdPost = response.as(Post.class);

        assertThat(response.statusCode())
                .as("Статус код должен быть 201 Created")
                .isEqualTo(201);

        assertThat(createdPost.getId())
                .as("ID созданного поста должен быть положительным")
                .isPositive();

        assertThat(createdPost.getTitle())
                .as("Заголовок должен совпадать")
                .isEqualTo("foo");

        assertThat(createdPost.getBody())
                .as("Тело поста должно совпадать")
                .isEqualTo("bar");

        assertThat(createdPost.getUserId())
                .as("ID пользователя должен быть 1")
                .isEqualTo(1);
    }

    @Test
    @DisplayName("Тест 2.1: Создание поста с пустыми данными")
    public void test2_createPostFailure() {
        Response response = postService.createPost(new HashMap<>());
        Post createdPost = response.as(Post.class);

        assertThat(response.statusCode())
                .as("Статус код должен быть 201 Created")
                .isEqualTo(201);

        assertThat(createdPost.getId())
                .as("ID созданного поста должен быть положительным")
                .isPositive();
    }

    @Test
    @DisplayName("Тест 2.1: Создание поста на невалидном эндпоинте")
    public void test2_createPostInvalidEndpoint() {
        Map<String, Object> postData = new HashMap<>();
        postData.put("title", "test");

        Response response = postService.createPostOnInvalidEndpoint(postData);

        assertThat(response.statusCode())
                .as("Статус код должен быть 404 Not Found")
                .isEqualTo(404);
    }

    @Test
    @DisplayName("Тест 3: Проверка сортировки постов по ID")
    public void test3_postsSortedById() {
        Response response = postService.getPostsResponse();
        List<Post> posts = Arrays.asList(response.as(Post[].class));

        assertThat(posts)
                .as("Список постов не должен быть пустым")
                .isNotEmpty();

        List<Integer> ids = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        assertThat(ids)
                .as("ID постов должны быть отсортированы по возрастанию")
                .isSorted();
    }

    @Test
    @DisplayName("Тест 4.1: Удаление поста")
    public void test4_deletePost() {
        Response response = postService.deletePost(2);

        assertThat(response.statusCode())
                .as("Статус код должен быть 200 OK")
                .isEqualTo(200);
    }

    @Test
    @DisplayName("Тест 4.2: Обновление поста и проверка данных")
    public void test4_updatePostAndValidate() {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("title", "Updated Title");
        updatedData.put("body", "Updated Body");

        Response response = postService.patchUpdatePost(1, updatedData);
        Post updatedPost = response.as(Post.class);

        assertThat(response.statusCode())
                .as("Статус код должен быть 200 OK")
                .isEqualTo(200);

        assertThat(updatedPost.getTitle())
                .as("Заголовок должен обновиться")
                .isEqualTo("Updated Title");

        assertThat(updatedPost.getBody())
                .as("Тело поста должно обновиться")
                .isEqualTo("Updated Body");
    }
}