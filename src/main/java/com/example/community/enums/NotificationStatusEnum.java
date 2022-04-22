package com.example.community.enums;

public enum NotificationStatusEnum {
    /**
     * unread
     */
    UNREAD(0),
    /**
     * read
     */
    READ(1);
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
