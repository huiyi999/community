package com.example.community.service.impl;

import com.example.community.dto.CommentDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.enums.NotificationStatusEnum;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.exception.CustomizedException;
import com.example.community.exception.impl.CustomizedErrorCodeImpl;
import com.example.community.mapper.*;
import com.example.community.model.*;
import com.example.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : chy
 * @date: 2022-04-17 5:01 p.m.
 */

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostExtMapper postExtMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizedException(CustomizedErrorCodeImpl.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizedException(CustomizedErrorCodeImpl.TYPE_PARAM_WRONG);
        }

        // 1. reply post, ParentId is post id
        if (Objects.equals(comment.getType(), CommentTypeEnum.POST.getType())) {
            System.out.println("insert post reply");
            Post post = postMapper.selectByPrimaryKey(comment.getParentId());
            if (post == null) {
                throw new CustomizedException(CustomizedErrorCodeImpl.POST_NOT_FOUND);
            }

            // increase comment count in comment
            // comment.setCommentCount(0);
            commentMapper.insert(comment);
            // increase comment count in post
            post.setCommentCount(1);
            postExtMapper.incCommentCount(post);

            createNotify(comment, post.getCreator(), commentator.getUsername(), post, NotificationTypeEnum.REPLY_POST);
        } else {
            // 2.reply comment, ParentId is comment id
            System.out.println("insert comment reply");
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizedException(CustomizedErrorCodeImpl.COMMENT_NOT_FOUND);
            }

            Post post = postMapper.selectByPrimaryKey(dbComment.getParentId());
            if (post == null) {
                throw new CustomizedException(CustomizedErrorCodeImpl.POST_NOT_FOUND);
            }

            // if comment and post exist, then insert
            commentMapper.insert(comment);

            // increase comment count
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            createNotify(comment, dbComment.getCommentator(), commentator.getUsername(), post, NotificationTypeEnum.REPLY_COMMENT);
        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, Post post, NotificationTypeEnum notificationType) {
        // if (receiver == comment.getCommentator()) {
        //     return;
        // }
        System.out.println("comment: " + notifierName);
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setReceiver(receiver);
        notification.setNotifier(comment.getCommentator());
        notification.setNotifierName(notifierName);
        notification.setOuterid(post.getId());
        notification.setType(notificationType.getType());
        notification.setOuterTitle(post.getTitle());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
    }

    @Override
    public void incLikeCount(Long id) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setLikeCount(1);
        System.out.println("impl inclike id: " + id);
        // System.out.println("impl inclike parent_id: " + id);
        commentExtMapper.incLikeCount(comment);
    }

    @Override
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");

        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        // get commentators set
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        // get commentators store in  Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // change comment to commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
