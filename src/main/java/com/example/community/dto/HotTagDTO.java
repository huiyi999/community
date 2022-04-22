package com.example.community.dto;

import lombok.Data;

/**
 * @author : chy
 * @date: 2022-04-22 1:32 p.m.
 */

@Data
public class HotTagDTO implements Comparable {
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDTO) o).getPriority();
    }
}
