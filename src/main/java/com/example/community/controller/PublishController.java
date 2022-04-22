package com.example.community.controller;

import com.example.community.cache.TagCache;
import com.example.community.dto.PostDTO;
import com.example.community.exception.CustomizedException;
import com.example.community.exception.impl.CustomizedErrorCodeImpl;
import com.example.community.model.Post;
import com.example.community.model.User;
import com.example.community.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

/**
 * @author chy
 */
@Controller
public class PublishController {

    @Autowired
    private PostService postService;

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "id", required = false) Long id,
                            Model model) {

        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "Title cannot be empty.");
            return "publish";
        }

        if (StringUtils.length(title) > 50) {
            model.addAttribute("error", "Title can't exceed 50 characters.");
            return "publish";
        }
        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "Description cannot be empty.");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "Tag cannot be empty.");
            return "publish";
        }

        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "Invalid tag:" + invalid);
            return "publish";
        }

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setDescription(description);
        post.setTag(tag);
        post.setCreator(user.getId());
        postService.createOrUpdate(post);
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model) {
        System.out.println("edit publish/id: " + id);
        PostDTO postDTO = postService.getById(id);

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (postDTO != null) {
            if (!Objects.equals(postDTO.getCreator(), user.getId())) {
                throw new CustomizedException(CustomizedErrorCodeImpl.INVALID_OPERATION);
            }
        }

        model.addAttribute("title", postDTO.getTitle());
        model.addAttribute("description", postDTO.getDescription());
        model.addAttribute("tag", postDTO.getTag());
        model.addAttribute("id", postDTO.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }
}
