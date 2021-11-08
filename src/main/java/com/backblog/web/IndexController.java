package com.backblog.web;

import com.backblog.po.Blog;
import com.backblog.service.BlogService;
import com.backblog.service.TagService;
import com.backblog.service.TypeService;
import com.backblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 15, sort = {"publishTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, BlogQuery blog) {
        blog.setPublished(true);
        Page<Blog> page =  blogService.listBlog(pageable, blog);

        model.addAttribute("page", page);
        model.addAttribute("pageTotal", page.getTotalPages());
        model.addAttribute("types", typeService.ListTypeTop(6));
        model.addAttribute("tags", tagService.ListTagTop(10));
        model.addAttribute("recent", blogService.listRecentBlog(6));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 15, sort = {"publishTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         Model model, @RequestParam String query) {
        Page<Blog> page =  blogService.listBlog(pageable, query);

        model.addAttribute("page", page);
        model.addAttribute("pageTotal", page.getTotalPages());
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/author")
    public String author() {
        return "author";
    }
}
