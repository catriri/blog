package com.backblog.service;

import com.backblog.dao.CommentRepository;
import com.backblog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> ListCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "publishTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentIsNull(blogId, sort);
        return reformComments(comments);
    }

    private List<Comment> reformComments(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment comment1 = new Comment();
            BeanUtils.copyProperties(comment, comment1);
            commentsView.add(comment1);
        }
        combineSons(commentsView);
        return commentsView;
    }

    private List<Comment> tempSons = new ArrayList<>();

    private void combineSons(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> sons = comment.getSonComments();
            if (sons.size() > 0) {
                for (Comment son : sons) {
                    tempSons.add(son);
                    addSonsRecursively(son);
                }
            }
            List<Comment> newSons = new ArrayList<>(tempSons);
            comment.setSonComments(newSons);
            tempSons.clear();
            System.out.println("parent comment has " + comment.getSonComments().size() + " son");
        }
    }

    private void addSonsRecursively(Comment comment) {
        List<Comment> sons = comment.getSonComments();
        if (sons.size() > 0) {
            for (Comment son : sons) {
                tempSons.add(son);
                addSonsRecursively(son);
            }
        }
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentId = -1L;
        if (comment.getParentComment() != null) {
            parentId = comment.getParentComment().getId();
        }
        if (parentId != -1) {
            comment.setParentComment(commentRepository.getById(parentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setPublishTime(new Date());
        return commentRepository.save(comment);
    }
}
