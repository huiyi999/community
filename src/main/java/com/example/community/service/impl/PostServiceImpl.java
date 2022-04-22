package com.example.community.service.impl;

import com.example.community.dto.PaginationDTO;
import com.example.community.dto.PostDTO;
import com.example.community.dto.QueryDTO;
import com.example.community.enums.SortTypeEnum;
import com.example.community.exception.CustomizedException;
import com.example.community.exception.impl.CustomizedErrorCodeImpl;
import com.example.community.mapper.PostExtMapper;
import com.example.community.model.Post;
import com.example.community.model.PostExample;
import com.example.community.model.User;
import com.example.community.mapper.UserMapper;
import com.example.community.mapper.PostMapper;
import com.example.community.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostExtMapper postExtMapper;

    @Override
    public PaginationDTO<PostDTO> list(Integer page, Integer size, String sort, String search, String tag) {
        QueryDTO queryDTO = new QueryDTO();
        PaginationDTO<PostDTO> paginationDTO = new PaginationDTO<>();
        // size * (page - 1)
        Integer offset = size * (page - 1);

        if (StringUtils.isNotBlank(search)) {
            String[] keywords = StringUtils.split(search, ",");
            search = Arrays.stream(keywords)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }

        queryDTO.setSearch(search);

        System.out.println("search: " + search);

        if (StringUtils.isNotBlank(tag)) {
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            queryDTO.setTag(tag);
        }

        Integer totalCount = postExtMapper.countBySearch(queryDTO);
        paginationDTO.setPagination(totalCount, page, size);


        if (StringUtils.isNotBlank(sort)) {
            for (SortTypeEnum sortEnum : SortTypeEnum.values()) {
                if (sortEnum.name().toLowerCase().equals(sort)) {
                    queryDTO.setSort(sort);
                    break;
                }
            }
        }

        queryDTO.setSize(size);
        queryDTO.setPage(offset);
        List<Post> postList = postExtMapper.selectBySearch(queryDTO);

        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : postList) {
            User user = userMapper.selectByPrimaryKey(post.getCreator());
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post, postDTO);
            postDTO.setUser(user);
            postDTOS.add(postDTO);
        }
        paginationDTO.setData(postDTOS);

        return paginationDTO;
    }

    @Override
    public PaginationDTO<PostDTO> list(Long userId, Integer page, Integer size) {
        // size * (page - 1)
        Integer offset = size * (page - 1);

        PostExample postExample = new PostExample();
        postExample.createCriteria().andCreatorEqualTo(userId);

        Integer totalCount = (int) postMapper.countByExample(postExample);
        System.out.println("totalCount: " + totalCount);

        PaginationDTO<PostDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(totalCount, page, size);


        List<Post> postList = postMapper.selectByExampleWithRowbounds(postExample, new RowBounds(offset, size));

        System.out.println("total post: " + postList.size());

        User user = userMapper.selectByPrimaryKey(userId);
        System.out.println("username: " + user.getUsername());
        System.out.println("url: " + user.getAvatarUrl());

        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : postList) {
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post, postDTO);
            postDTO.setUser(user);
            postDTOS.add(postDTO);
        }
        paginationDTO.setData(postDTOS);

        return paginationDTO;
    }

    @Override
    public PostDTO getById(Long id) {
        System.out.println("PostDTO getById: id = " + id);
        Post post = postMapper.selectByPrimaryKey(id);
        if (post == null) {
            throw new CustomizedException(CustomizedErrorCodeImpl.POST_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(post.getCreator());

        PostDTO postDTO = new PostDTO();
        // System.out.println("post service getById post.getCreator():" + post.getCreator());
        BeanUtils.copyProperties(post, postDTO);
        // System.out.println(postDTO.toString());
        postDTO.setUser(user);

        // System.out.println(postDTO.toString());
        return postDTO;
    }

    @Override
    public void createOrUpdate(Post post) {
        if (post.getId() == null) {
            // create
            post.setGmtCreate(System.currentTimeMillis());
            post.setGmtModified(post.getGmtCreate());
            post.setViewCount(0);
            post.setLikeCount(0);
            post.setCommentCount(0);
            postMapper.insert(post);
        } else {

            Post dbPost = postMapper.selectByPrimaryKey(post.getId());

            // post not exist
            if (dbPost == null) {
                throw new CustomizedException(CustomizedErrorCodeImpl.POST_NOT_FOUND);
            }

            // current can't update post
            if (dbPost.getCreator().longValue() != post.getCreator().longValue()) {
                throw new CustomizedException(CustomizedErrorCodeImpl.INVALID_OPERATION);
            }

            Post updatePost = new Post();   // if use parameter Post, will modify all cloumn
            updatePost.setGmtModified(System.currentTimeMillis());
            updatePost.setTitle(post.getTitle());
            updatePost.setDescription(post.getDescription());
            updatePost.setTag(post.getTag());
            PostExample postExample = new PostExample();
            postExample.createCriteria().andIdEqualTo(post.getId());


            int updated = postMapper.updateByExampleSelective(updatePost, postExample);
            if (updated != 1) {
                throw new CustomizedException(CustomizedErrorCodeImpl.POST_NOT_FOUND);

            }
        }

    }

    @Override
    public void incViewCount(Long id) {
        // Post post = postMapper.selectByPrimaryKey(id);
        // Post updatePost = new Post();
        // PostExample postExample = new PostExample();
        // postExample.createCriteria().andIdEqualTo(id);
        // updatePost.setViewCount(post.getViewCount() + 1);   // concurrency
        // postMapper.updateByExampleSelective(updatePost, postExample);
        Post post = new Post();
        post.setId(id);
        post.setViewCount(1);
        postExtMapper.incViewCount(post);
    }

    @Override
    public void incLikeCount(Long id) {
        Post post = new Post();
        post.setId(id);
        post.setLikeCount(1);
        postExtMapper.incLikeCount(post);
    }

    @Override
    public List<PostDTO> selectRelated(PostDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag()))
            return new ArrayList<>();

        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        tags = StringUtils.stripAll(tags);  // remove whitespace

        String regexpTag = Arrays.stream(tags)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));

        Post post = new Post();
        post.setId(queryDTO.getId());
        post.setTag(regexpTag);

        List<Post> posts = postExtMapper.selectRelated(post);
        List<PostDTO> postDTOS = posts.stream().map(p -> {
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(p, postDTO);
            return postDTO;
        }).collect(Collectors.toList());

        return postDTOS;
    }

}
