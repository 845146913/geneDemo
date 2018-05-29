package com.example.demo.support;

import com.example.demo.support.entity.GenericEntity;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;

public interface GenericRepository<E extends GenericEntity<PK>, PK extends Serializable> extends Mapper<E>, IdsMapper<E> {
}
