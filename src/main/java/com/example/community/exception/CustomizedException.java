package com.example.community.exception;

/**
 * @author : chy
 * @date: 2022-04-16 8:02 p.m.
 */
public class CustomizedException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizedException(CustomizedErrorCode customizedErrorCode) {
        this.message = customizedErrorCode.getMessage();
        this.code = customizedErrorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
