package com.backblog.service;

import com.backblog.po.Blog;
import com.backblog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlogByTagId(Pageable pageable, Long tagId);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, String query);

    List<Blog> listRecentBlog(Integer size);

    Map<String, List<Blog>> archiveBLog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    Long countBlog();

    void deleteBlog(Long id);
}
