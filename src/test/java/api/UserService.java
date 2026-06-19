package api;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static api.BaseSpecifications.getBaseSpec;

@Slf4j
public class UserService {

    private static final String USERS = "/users";
    private static final String USER_BY_ID = "/users/{id}";

    public Response getUsersResponse() {
        log.info("GET запрос к {}", USERS);
        return getBaseSpec()
                .when()
                .get(USERS);
    }

    public Response getUserByIdResponse(int userId) {
        log.info("GET запрос к {}", USER_BY_ID.replace("{id}", String.valueOf(userId)));
        return getBaseSpec()
                .when()
                .get(USER_BY_ID, userId);
    }
}