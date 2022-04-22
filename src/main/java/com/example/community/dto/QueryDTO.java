package com.example.community.dto;

import lombok.Data;

/**
 * @author : chy
 * @date: 2022-04-21 4:01 p.m.
 */

@Data
public class QueryDTO {

    private String search;
    private String sort;
    private Integer page;
    private Integer size;
    private String tag;
}
