package com.example.community.controller;

import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;
import com.example.community.dto.PostDTO;
import com.example.community.model.Notification;
import com.example.community.model.User;
import com.example.community.service.NotificationService;
import com.example.community.service.PostService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author : chy
 * @date: 2022-04-14 9:47 p.m.
 */

@Controller
public class ProfileController {

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationService notificationService;

    // dynamic get
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          HttpServletRequest request,
                          Model model) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        System.out.println("/profile/{action}" + action);

        if ("posts".equals(action)) {
            model.addAttribute("section", "posts");
            model.addAttribute("sectionName", "My posts");

            PaginationDTO<PostDTO> paginationDTO = postService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "Recent replies");

            PaginationDTO<NotificationDTO> paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        }
        Long unreadCount = notificationService.unreadCount(user.getId());
        request.getSession().setAttribute("unreadCount", unreadCount);
        return "profile";
    }
}
