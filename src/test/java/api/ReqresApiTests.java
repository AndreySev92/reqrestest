package api;

import dto.Post;
import dto.User;
import org.testng.annotations.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;  // ← заменили

public class ReqresApiTests extends BaseTest {

    private UserService userService = new UserService();
    private PostService postService = new PostService();

    @Test
    public void test1_getUsersAndValidateWithDto() {
        List<User> users = userService.getUsersAsDto();

        assertThat(users).isNotEmpty();

        for (User user : users) {
            assertThat(user.getId()).isPositive();
            assertThat(user.getName()).isNotBlank();
            assertThat(user.getEmail())
                    .isNotBlank()
                    .contains("@");
        }
    }

    @Test
    public void testGetPostByIdWithDto() {
        Post post = postService.getPostByIdAsDto(1);

        assertThat(post.getId()).isEqualTo(1);
        assertThat(post.getTitle()).isNotBlank();
        assertThat(post.getBody()).isNotBlank();
        assertThat(post.getUserId()).isPositive();
    }
}