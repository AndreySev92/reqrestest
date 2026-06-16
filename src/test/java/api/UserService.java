package api;

import io.restassured.response.Response;

import static api.BaseSpecifications.getBaseSpec;

public class UserService {

    private static final String USERS = "/users";
    private static final String USER_BY_ID = "/users/{id}";

    public Response getUsersResponse() {
        return getBaseSpec()
                .when()
                .get(USERS);
    }

    public Response getUserByIdResponse(int userId) {
        return getBaseSpec()
                .when()
                .get(USER_BY_ID, userId);
    }
}