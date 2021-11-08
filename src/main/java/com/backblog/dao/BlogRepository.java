package com.backblog.dao;

import com.backblog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    Blog getBlogById(Long id);

    @Query("select b from Blog b where b.published = true")
    List<Blog> findTop(Pageable pageable);

    // select * from t_blog where title like '%xxx%'
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(Pageable pageable, String query);

    @Modifying
    @Query("update Blog b set b.viewCount = b.viewCount + 1 where b.id = ?1")
    void updateViewCount(Long id);

    @Query("select function('date_format', b.publishTime, '%Y') as year from Blog b " +
            "group by function('date_format', b.publishTime, '%Y') " +
            "order by function('date_format', b.publishTime, '%Y') desc")
    List<String> findYears();

    @Query("select b from Blog b where function('date_format', b.publishTime, '%Y') = ?1")
    List<Blog> findByYear(String year);
}
