package com.example.community.enums;

import java.util.Objects;

/**
 * @author : chy
 * @date: 2022-04-17 4:56 p.m.
 */

public enum CommentTypeEnum {
    /**
     * post
     */
    POST(1),
    /**
     * comment
     */
    COMMENT(2);
    private Integer type;

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (Objects.equals(commentTypeEnum.getType(), type)) {
                return true;
            }
        }
        return false;
    }
}
