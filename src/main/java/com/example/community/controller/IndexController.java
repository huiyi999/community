package com.example.community.controller;

import com.example.community.cache.HotTagCache;
import com.example.community.dto.PaginationDTO;
import com.example.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author chy
 */
@Controller
public class IndexController {

    @Autowired
    private PostService postService;

    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping({"/", "/index"})
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        System.out.println("sort: " + sort);
        System.out.println("search: " + search);
        System.out.println("tag: " + tag);

        PaginationDTO pagination = postService.list(page, size, sort, search, tag);
        List<String> hotTags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("hotTags", hotTags);
        model.addAttribute("sort", sort);
        model.addAttribute("tag", tag);
        return "index";
    }

    @RequestMapping("/toLoginPage")
    public String toLoginPage() {
        return "user/login";
    }

    @RequestMapping("/toRegisterPage")
    public String toRegisterPage() {
        return "user/register";
    }
}
