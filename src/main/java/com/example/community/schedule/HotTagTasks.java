package com.example.community.schedule;

import com.example.community.cache.HotTagCache;
import com.example.community.mapper.PostMapper;
import com.example.community.model.Post;
import com.example.community.model.PostExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author : chy
 * @date: 2022-04-22 12:51 p.m.
 */

@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 5;
        log.info("hotTagSchedule start: " + new Date());
        List<Post> postList = new ArrayList<>();
        Map<String, Integer> priorities = new HashMap<>();

        while (offset == 0 || postList.size() == limit) {
            postList = postMapper.selectByExampleWithRowbounds(new PostExample(), new RowBounds(offset, limit));
            for (Post post : postList) {

                String[] tags = StringUtils.split(post.getTag(), ",");
                for (String tag : tags) {

                    Integer priority = priorities.get(tag);
                    // priority = postCount * 5 + commentCount
                    if (priority != null) {
                        priorities.put(tag, priority * 5 + post.getCommentCount());
                    } else {
                        priorities.put(tag, 5 + post.getCommentCount());
                    }
                }
            }
            hotTagCache.updateTags(priorities);
            // priorities.forEach(
            //         (k, v) -> {
            //             System.out.print(k);
            //             System.out.print(":");
            //             System.out.print(v);
            //             System.out.println();
            //         }
            //
            // );
            offset += limit;
        }

        log.info("hotTagSchedule stop: " + new Date());
    }


}
