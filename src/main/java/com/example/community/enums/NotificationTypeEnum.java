package com.example.community.enums;

/**
 * @author : chy
 * @date: 2022-04-20 9:25 p.m.
 */
public enum NotificationTypeEnum {
    /**
     * replies post
     */
    REPLY_POST(1, "replies post"),
    /**
     * replies comment
     */
    REPLY_COMMENT(2, "replies comment");
    private int type;
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String nameOfType(int type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type) {
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }
}
