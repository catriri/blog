package com.backblog.service;

import com.backblog.dao.TagRepository;
import com.backblog.dao.TypeRepository;
import com.backblog.po.Tag;

import com.backblog.web.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getTagById(id);
    }

    @Transactional
    @Override
    public Page<Tag> ListTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> ListTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> ListTag(String idList) { // 1, 2, 3
        return tagRepository.findAllById(convertToList(idList));
    }

    @Override
    public List<Tag> ListTagTop(Integer size) {
        Sort sort  = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }

    private List<Long> convertToList(String idList) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(idList)){
            String[] idArr = idList.split(",");
            for (String id : idArr){
                list.add(new Long(id.trim()));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getTagById(id);
        if (t == null) {
            throw new NotFoundException("Tag not found");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findTagByName(name);
    }

}
