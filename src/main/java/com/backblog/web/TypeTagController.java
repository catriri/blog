package com.backblog.web;

import com.backblog.po.Type;
import com.backblog.service.BlogService;
import com.backblog.service.TagService;
import com.backblog.service.TypeService;
import com.backblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TypeTagController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/sort")
    public String types(@PageableDefault(size = 15, sort = {"publishTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("types", typeService.ListType());
        model.addAttribute("active", -1);
        model.addAttribute("page", blogService.listBlog(pageable));
        return "/sort";
    }

    @GetMapping("/label")
    public String tags(@PageableDefault(size = 15, sort = {"publishTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("tags", tagService.ListTag());
        model.addAttribute("active", -1);
        model.addAttribute("page", blogService.listBlog(pageable));
        return "/label";
    }

    @GetMapping("/sort/{id}")
    public String types(@PageableDefault(size = 15, sort = {"publishTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, @PathVariable Long id, BlogQuery blog) {
        if(id != -1){
            blog.setTypeId(id);
            model.addAttribute("page", blogService.listBlog(pageable, blog));
        } else {
            model.addAttribute("page", blogService.listBlog(pageable));
        }

        model.addAttribute("types", typeService.ListTypeTop(100));
        model.addAttribute("active", id);
        return "/sort";
    }

    @GetMapping("/label/{id}")
    public String tags(@PageableDefault(size = 15, sort = {"publishTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, @PathVariable Long id, BlogQuery blog) {
        if(id != -1){
            model.addAttribute("page", blogService.listBlogByTagId(pageable, id));
        } else {
            model.addAttribute("page", blogService.listBlog(pageable));
        }

        model.addAttribute("tags", tagService.ListTagTop(100));
        model.addAttribute("active", id);
        return "/label";
    }

}
