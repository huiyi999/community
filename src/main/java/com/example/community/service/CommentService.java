package com.example.community.service;

import com.example.community.dto.CommentCreateDTO;
import com.example.community.dto.CommentDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.model.Comment;
import com.example.community.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : chy
 * @date: 2022-04-17 4:59 p.m.
 */

@Service
public interface CommentService {


    void insert(Comment comment, User user);

    void incLikeCount(Long id);

    List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type);
}
