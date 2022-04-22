package com.example.community.mapper;

import com.example.community.model.Comment;
import com.example.community.model.Post;


/**
 * @author : chy
 * @date: 2022-04-17 9:39 p.m.
 */
public interface CommentExtMapper {
    int incCommentCount(Comment comment);

    void incLikeCount(Comment comment);
}
