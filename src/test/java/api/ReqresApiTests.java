package api;

import dto.Post;
import dto.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("API тесты для JSONPlaceholder")
public class ReqresApiTests {

    private final UserService userService;
    private final PostService postService;

    public ReqresApiTests() {
        this.userService = new UserService();
        this.postService = new PostService();
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

    @ParameterizedTest
    @CsvSource({
            "1, Leanne Graham",
            "2, Ervin Howell",
            "3, Clementine Bauch"
    })
    @DisplayName("Тест 1.1: Получить пользователя по ID и проверить имя")
    public void test1_getUserByIdAndValidateName(int userId, String expectedName) {

        Response response = userService.getUserByIdResponse(userId);
        User user = response.as(User.class);

        assertThat(response.statusCode())
                .as(ValidationMessages.STATUS_CODE_OK)
                .isEqualTo(200);

        assertThat(user.getName())
                .as(ValidationMessages.USER_FOUND)
                .isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Тест 2.1: Успешное создание поста")
    public void test2_createPostSuccess() {
        Response response = postService.createPost(TestDataProvider.getValidPostData());
        Post createdPost = response.as(Post.class);

        // THEN
        assertThat(response.statusCode())
                .as(ValidationMessages.STATUS_CODE_OK)
                .isEqualTo(201);

        // ASSERT
        assertThat(createdPost.getId())
                .as("ID созданного поста должен быть положительным")
                .isPositive();

        assertThat(createdPost.getTitle())
                .as("Заголовок должен совпадать")
                .isEqualTo(TestDataProvider.getExpectedTitle());

        assertThat(createdPost.getBody())
                .as("Тело поста должно совпадать")
                .isEqualTo(TestDataProvider.getExpectedBody());

        assertThat(createdPost.getUserId())
                .as("ID пользователя должен быть 1")
                .isEqualTo(TestDataProvider.getExpectedUserId());
    }

    @Test
    @DisplayName("Тест 2.2: Создание поста с пустыми данными")
    public void test2_createPostFailure() {
        Response response = postService.createPost(TestDataProvider.getEmptyPostData());
        Post createdPost = response.as(Post.class);

        assertThat(response.statusCode())
                .as("Статус код должен быть 201 Created")
                .isEqualTo(201);

        assertThat(createdPost.getId())
                .as("ID созданного поста должен быть положительным")
                .isPositive();
    }

    @Test
    @DisplayName("Тест 2.3: Создание поста на невалидном эндпоинте")
    public void test2_createPostInvalidEndpoint() {

        Response response = postService.createPostOnInvalidEndpoint(TestDataProvider.getValidPostData());


        assertThat(response.statusCode())
                .as(ValidationMessages.STATUS_CODE_404)
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
        int postId = TestDataProvider.getPostIdToDelete();

        Response response = postService.deletePost(postId);

        assertThat(response.statusCode())
                .as(ValidationMessages.STATUS_CODE_OK)
                .isEqualTo(200);
    }


    @Test
    @DisplayName("Тест 4.2: Обновление поста и проверка данных")
    public void test4_updatePostAndValidate() {
        int postId = TestDataProvider.getPostIdToDelete();
        String newTitle = TestDataProvider.getUpdateTitle();
        String newBody = TestDataProvider.getUpdateBody();

        // WHEN
        Response response = postService.patchUpdatePost(
                postId,
                TestDataProvider.getUpdatePostData(newTitle, newBody)
        );
        Post updatedPost = response.as(Post.class);

        // THEN
        assertThat(response.statusCode())
                .as(ValidationMessages.STATUS_CODE_OK)
                .isEqualTo(200);

        // ASSERT
        assertThat(updatedPost.getTitle())
                .as("Заголовок должен обновиться")
                .isEqualTo(newTitle);

        assertThat(updatedPost.getBody())
                .as("Тело поста должно обновиться")
                .isEqualTo(newBody);
    }

    @Test
    @DisplayName("Тест 5: Получение несуществующего пользователя (404)")
    public void test5_getNonExistentUser() {
        Response response = userService.getUserByIdResponse(TestDataProvider.getNonExistentUserId());

        assertThat(response.statusCode())
                .as(ValidationMessages.STATUS_CODE_404)
                .isEqualTo(404);
    }
}