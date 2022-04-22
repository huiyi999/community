package com.example.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author : chy
 * @date: 2022-04-20 4:11 p.m.
 */

@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
