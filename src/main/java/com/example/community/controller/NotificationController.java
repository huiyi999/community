package com.example.community.controller;

import com.example.community.dto.NotificationDTO;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.model.User;
import com.example.community.service.NotificationService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : chy
 * @date: 2022-04-20 11:09 p.m.
 */

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping({"/notification/{id}"})
    public String readNotification(@PathVariable(name = "id") Long id) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        NotificationDTO notificationDTO = notificationService.read(user.getId(), id);

        if (notificationDTO.getType() == NotificationTypeEnum.REPLY_COMMENT.getType()
                || notificationDTO.getType() == NotificationTypeEnum.REPLY_POST.getType()) {
            return "redirect:/post/" + notificationDTO.getOuterid();
        } else {
            return "redirect:/";
        }
    }
}
