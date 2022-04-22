package com.example.community.service;


import com.example.community.dto.PaginationDTO;
import com.example.community.dto.PostDTO;
import com.example.community.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    PaginationDTO<PostDTO> list(Integer page, Integer size, String sort, String search,String tag);

    PaginationDTO<PostDTO> list(Long userId, Integer page, Integer size);

    PostDTO getById(Long id);

    void createOrUpdate(Post post);

    void incViewCount(Long id);

    void incLikeCount(Long id);

    List<PostDTO> selectRelated(PostDTO queryDTO);
}
