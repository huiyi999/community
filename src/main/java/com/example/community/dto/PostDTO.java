package com.example.community.dto;

import com.example.community.model.User;
import lombok.Data;

@Data
public class PostDTO {

    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private User user;
}
