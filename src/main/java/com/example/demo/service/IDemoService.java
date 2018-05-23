package com.example.demo.service;

import com.example.demo.support.dto.GenericDTO;
import com.example.demo.support.service.IGenericService;
import com.example.demo.model.Demo;

public interface IDemoService extends IGenericService<Demo, Integer>{

    <S extends GenericDTO<Integer>> S create(S dto);

    String getEntityName();
}
