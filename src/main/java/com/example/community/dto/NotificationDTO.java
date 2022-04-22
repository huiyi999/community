package com.example.community.dto;

import lombok.Data;

/**
 * @author : chy
 * @date: 2022-04-20 9:56 p.m.
 */

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
