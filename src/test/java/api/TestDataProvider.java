package api;

import dto.User;

import java.util.HashMap;
import java.util.Map;

public class TestDataProvider {
    public static User getValidUser() {
        return User.builder()
                .name("John Doe")
                .username("johndoe")
                .email("john@example.com")
                .phone("123-456-7890")
                .website("johndoe.com")
                .build();
    }

    public static User getInvalidUser() {
        return User.builder()
                .name("")
                .username("")
                .email("invalid-email")
                .build();
    }

    public static String getUpdateTitle() {
        return "Updated Title";
    }

    public static String getUpdateBody() {
        return "Updated Body";
    }

    public static Map<String, Object> getValidPostData() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "foo");
        data.put("body", "bar");
        data.put("userId", 1);
        return data;
    }

    public static Map<String, Object> getEmptyPostData() {
        return new HashMap<>();
    }

    public static Map<String, Object> getUpdatePostData(String title, String body) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);
        return data;
    }

    public static String getExpectedTitle() {return "foo";}
    public static String getExpectedBody() {return "bar";}
    public static int getExpectedUserId() {return 1;}
    public static int getPostIdToDelete() {return 2;}
    public static int getNonExistentUserId() {return 99999;}

}