package com.example.demo.service;

import com.example.demo.support.repository.GenericRepository;
import com.example.demo.support.dto.GenericDTO;
import com.example.demo.support.service.GenericService;
import com.example.demo.dto.DemoDTO;
import com.example.demo.mapper.DemoMapper;
import com.example.demo.model.Demo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DemoService extends GenericService<Demo, Integer> implements IDemoService {

    @Resource
    private DemoMapper demoMapper;
    @Override
    protected GenericRepository<Demo, Integer> getRepository() {
        return demoMapper;
    }

    @Override
    public <S extends GenericDTO<Integer>> S create(S dto) {
        DemoDTO d = (DemoDTO) dto;
        return (S) d;
    }

    @Override
    public String getEntityName() {
        return tClass.getName();
    }
}
