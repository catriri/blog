package com.backblog.service;

import com.backblog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type save(Type type);

    Type getType(Long id);

    List<Type> ListType();

    List<Type> ListTypeTop(Integer size);

    Type updateType(Long id, Type type);

    void deleteType(Long id);

    Type findByName(String name);
}
