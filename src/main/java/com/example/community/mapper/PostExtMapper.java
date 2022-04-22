package com.example.community.mapper;

import com.example.community.dto.QueryDTO;
import com.example.community.model.Post;

import java.util.List;

public interface PostExtMapper {
    int incViewCount(Post record);

    void incCommentCount(Post record);

    List<Post> selectRelated(Post record);

    void incLikeCount(Post record);

    Integer countBySearch(QueryDTO queryDTO);

    List<Post> selectBySearch(QueryDTO queryDTO);
}