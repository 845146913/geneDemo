package com.example.demo.support.controller;

import com.example.demo.support.entity.GenericEntity;
import com.example.demo.support.service.IGenericService;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;
import java.util.List;

public abstract class GenericController<E extends GenericEntity<PK>, PK extends Serializable> {
    // 映射路径
    private String path;
    protected abstract IGenericService<E, PK> getService();
    public GenericController(String path) {
        this.path = path;
    }

    @GetMapping
    public List<E> findAll() throws Exception {
        return getService().findAll();
    }
}
