package com.backblog.web;

import com.backblog.po.Blog;
import com.backblog.po.Comment;
import com.backblog.po.User;
import com.backblog.service.BlogService;
import com.backblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.ListCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String reply(Comment comment, HttpSession session) {
        User user = (User)session.getAttribute("user");
        if (user != null){
            comment.setAdminComment(true);
            comment.setNickname("zkt");
        } else {
            comment.setAdminComment(false);
        }
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }
}
