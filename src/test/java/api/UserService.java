package api;

import dto.User;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

public class UserService extends BaseTest{

    private static final String USERS = "/users";
    private static final String USER_BY_ID = "/users/{id}";

    public Response getUsersRespose(){
        return getBaseSpec()
                .when()
                .get(USERS);
    }

    public List<User> getUsersAsDto(){
        return Arrays.asList(getBaseSpec()
                .when()
                .get(USERS)
                .then()
                .extract()
                .as(User[].class));
    }

    public Response getUserByIdResponse(int userId) {
        return getBaseSpec()
                .when()
                .get(USER_BY_ID, userId);
    }

    public User getUserByIdAsDto(int userId) {
        return getBaseSpec()
                .when()
                .get(USER_BY_ID, userId)
                .then()
                .extract()
                .as(User.class);
    }
}
