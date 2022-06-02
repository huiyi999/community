package com.example.community.controller;

import com.example.community.dto.CommentCreateDTO;
import com.example.community.dto.CommentDTO;
import com.example.community.dto.PostDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.exception.CustomizedException;
import com.example.community.exception.impl.CustomizedErrorCodeImpl;
import com.example.community.model.User;
import com.example.community.service.CommentService;
import com.example.community.service.NotificationService;
import com.example.community.service.PostService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : chy
 * @date: 2022-04-15 11:56 a.m.
 */

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/post/{id}")
    public String post(@PathVariable(name = "id") String id,
                       HttpServletRequest request,
                       Model model) {

        System.out.println("post id:" + id);
        Long postId = null;
        try {
            postId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new CustomizedException(CustomizedErrorCodeImpl.INVALID_INPUT);
        }
        PostDTO postDTO = postService.getById(postId);
        List<PostDTO> relatedPosts = postService.selectRelated(postDTO);
        List<CommentDTO> comments = commentService.listByTargetId(postId, CommentTypeEnum.POST);

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        // increment view count
        postService.incViewCount(postId);

        model.addAttribute("post", postDTO);
        model.addAttribute("user", user);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedPosts", relatedPosts);

        if (user != null) {
            Long unreadCount = notificationService.unreadCount(user.getId());
            request.getSession().setAttribute("unreadCount", unreadCount);
        }
        return "post";
    }
}
