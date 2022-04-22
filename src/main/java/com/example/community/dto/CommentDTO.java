package com.example.community.dto;

import com.example.community.model.User;
import lombok.Data;

/**
 * @author : chy
 * @date: 2022-04-18 5:56 p.m.
 */

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
