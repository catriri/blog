package com.backblog.web.admin;

import com.backblog.po.Blog;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blog_add";
    private static final String LIST = "admin/blogs_manage";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 15, sort = {"publishTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, BlogQuery blog) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        model.addAttribute("types", typeService.ListType());
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 15, sort = {"publishTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         Model model, BlogQuery blog) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs_manage :: blogList";
    }

    @GetMapping("/blogs/add")
    public String addBlog(Model model) {
        model.addAttribute("blog", new Blog());
        setTypeAndModel(model);
        return INPUT;
    }

    @GetMapping("/blogs/{id}/edit")
    public String editBlog(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        setTypeAndModel(model);
        return INPUT;
    }

    private void setTypeAndModel(Model model) {
        model.addAttribute("types", typeService.ListType());
        model.addAttribute("tags", tagService.ListTag());
    }

    @GetMapping("/blogs/{id}/delete")
    public String deleteType(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        if (id == null) {
            attributes.addFlashAttribute("message", "Deleting failed");
        } else {
            attributes.addFlashAttribute("message", "Deleting completed");
        }
        return REDIRECT_LIST;
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        // the type.id attribute was added to blog in the front end
        blog.setType(typeService.getType(blog.getType().getId()));
        if (!Objects.equals(blog.getTagIds(), "")) {
            blog.setTags(tagService.ListTag(blog.getTagIds()));
        }
        Blog b;
        if (blog.getId() != null) {
            b = blogService.updateBlog(blog.getId(), blog);
        } else {
            b = blogService.saveBlog(blog);
        }
        if (b == null) {
            attributes.addFlashAttribute("message", "Operation failed");
        } else {
            attributes.addFlashAttribute("message", "Operation completed");
        }
        return REDIRECT_LIST;
    }
}
