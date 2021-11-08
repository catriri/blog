package com.backblog.service;

import com.backblog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag save(Tag tag);

    Tag getTag(Long id);

    Page<Tag> ListTag(Pageable pageable);

    List<Tag> ListTag();

    List<Tag> ListTag(String idList);

    List<Tag> ListTagTop(Integer size);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    Tag findByName(String name);
}
