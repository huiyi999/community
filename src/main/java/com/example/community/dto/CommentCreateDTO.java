package com.example.community.dto;

import com.example.community.model.User;
import lombok.Data;

/**
 * @author : chy
 * @date: 2022-04-17 4:30 p.m.
 */

@Data
public class CommentCreateDTO {

    private Long parentId;
    private Integer type;
    private String content;
}
