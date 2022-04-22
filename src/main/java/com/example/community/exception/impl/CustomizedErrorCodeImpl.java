package com.example.community.exception.impl;

import com.example.community.exception.CustomizedErrorCode;

/**
 * @author : chy
 * @date: 2022-04-16 8:12 p.m.
 */


public enum CustomizedErrorCodeImpl implements CustomizedErrorCode {

    POST_NOT_FOUND(2001, "The post does not exist. Do you want to try another one?"),
    TARGET_PARAM_NOT_FOUND(2002, "No posts or comments are selected."),
    NO_LOGIN(2003, "Login is required. Please login and try again."),
    SYS_ERROR(2004, "System error！Please try again later！"),
    TYPE_PARAM_WRONG(2005, "Comment type error or nonexistent"),
    COMMENT_NOT_FOUND(2006, "The comment does not exist. Do you want to try another one?"),
    CONTENT_IS_EMPTY(2007, "The input cannot be empty."),
    READ_NOTIFICATION_FAIL(2008, "Failed to read notification."),
    NOTIFICATION_NOT_FOUND(2009, "The notification does not exist."),
    FILE_UPLOAD_FAIL(2010, "Failed to upload image."),
    INVALID_INPUT(2011, "Invalid input"),
    INVALID_OPERATION(2012, "Invalid operation."),
    USER_DISABLE(2013, "The operation is disabled. If you have any questions, contact the administrator."),
    RATE_LIMIT(2014, "Operation is too frequent. Please try again later."),
    ;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizedErrorCodeImpl(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
