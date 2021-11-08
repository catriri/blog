package com.backblog.service;

import com.backblog.dao.BlogRepository;
import com.backblog.po.Blog;
import com.backblog.po.Type;
import com.backblog.util.MarkdownUtils;
import com.backblog.util.NullPropertiesUtils;
import com.backblog.vo.BlogQuery;
import com.backblog.web.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getBlogById(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getBlogById(id);
        if (blog == null) {
            throw  new NotFoundException("blog does not exit");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViewCount(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlogByTagId(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(
                            cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }

                if (blog.getTypeId() != null) {
                    predicates.add(
                            cb.equal(root.<Type>get("type").get("id"), blog.getTypeId())
                    );
                }

                if (blog.isPublished()) {
                    predicates.add(
                            cb.equal(root.<Boolean>get("published"), true));
                }

                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery(pageable, "%" + query + "%");
    }

    @Override
    public List<Blog> listRecentBlog(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "publishTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBLog() {
        Map<String, List<Blog>> archiveMap = new HashMap<>();
        List<String> years = blogRepository.findYears();
        for (String year : years){
            archiveMap.put(year, blogRepository.findByYear(year));
        }
        return archiveMap;
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setViewCount(0);
        }
        blog.setPublishTime(new Date());
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getBlogById(id);
        if (b == null) {
            throw new NotFoundException("Blog not found");
        }
        //assign the value of blog to b
        BeanUtils.copyProperties(blog, b, NullPropertiesUtils.getNullPropertyNames(blog));
        b.setPublishTime(new Date());
        return blogRepository.save(b);
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
