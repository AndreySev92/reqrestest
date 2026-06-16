package dto;

import lombok.Data;

@Data
public class Post {
    private int id;
    private String title;
    private String body;
    private int userId;
}