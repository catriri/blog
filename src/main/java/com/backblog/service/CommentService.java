package com.backblog.service;

import com.backblog.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> ListCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
