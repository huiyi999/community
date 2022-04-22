package com.example.community.controller;

import com.example.community.dto.CommentCreateDTO;
import com.example.community.dto.CommentDTO;
import com.example.community.dto.ResultDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.exception.impl.CustomizedErrorCodeImpl;
import com.example.community.model.Comment;
import com.example.community.model.User;
import com.example.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author : chy
 * @date: 2022-04-17 4:03 p.m.
 */

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @PostMapping(value = "/comment")
    public Object replyPost(@RequestBody CommentCreateDTO commentCreateDTO) {
        System.out.println("test reply post");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizedErrorCodeImpl.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setCommentCount(0);
        comment.setLikeCount(0);
        commentService.insert(comment, user);

        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public Object replyComment(@PathVariable(name = "id") Long id) {
        System.out.println("reply comment: " + id);
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        System.out.println("reply comment: ok");
        return ResultDTO.okOf(commentDTOS);
    }


    @ResponseBody
    @PostMapping(value = "/likeComment")
    public Object vote(@RequestParam(value = "id") Long id) {
        System.out.println("test likeComment");
        // if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
        //     return ResultDTO.errorOf(CustomizedErrorCodeImpl.CONTENT_IS_EMPTY);
        // }
        //
        // Long id = commentCreateDTO.getParentId();
        commentService.incLikeCount(id);

        return ResultDTO.okOf();
    }
}
