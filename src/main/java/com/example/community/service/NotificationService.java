package com.example.community.service;

import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;
import org.springframework.stereotype.Service;


/**
 * @author : chy
 * @date: 2022-04-20 9:59 p.m.
 */

@Service
public interface NotificationService {

    PaginationDTO<NotificationDTO> list(Long userId, Integer page, Integer size);

    Long unreadCount(Long userId);

    NotificationDTO read(Long userId, Long id);
}
