package com.example.demo.support.service;

import com.example.demo.support.entity.GenericEntity;

import java.io.Serializable;
import java.util.List;

public interface IGenericService<E extends GenericEntity<PK>, PK extends Serializable> {

    List<E> findAll();

    E findById(PK id);

    E save(E entity);

    <S extends E> List<S> batchSave(Iterable<S> entities);
}
