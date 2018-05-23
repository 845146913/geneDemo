package com.example.demo.support.service;

import com.example.demo.support.GenericRepository;
import com.example.demo.support.entity.GenericEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 通用抽象service
 * @param <T>
 * @param <PK>
 */
@Slf4j
public abstract class GenericService<T extends GenericEntity<PK>, PK  extends Serializable> implements IGenericService<T,PK>{



    protected Class<T> tClass;

    protected abstract GenericRepository<T, PK> getRepository();
    public GenericService() {
        Type type = getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) type;
            tClass = (Class<T>) t.getActualTypeArguments()[0];
            log.debug("genericService init entityClass:{}", tClass);
        }
    }

    @Override
    public List<T> findAll() {
        return getRepository().selectAll();
    }

    @Override
    public T findById(PK id) {
        return getRepository().selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public T save(T entity) {
        Objects.requireNonNull(entity);
        int result = getRepository().insert(entity);
        return result > 0 ? entity : null;
    }

    @Override
    @Transactional
    public <S extends T> List<S> batchSave(Iterable<S> entities) {
        List<S> result = new ArrayList<S>();

        if (entities == null) {
            return result;
        }
        entities.forEach( entity -> {
            getRepository().insert(entity);
            result.add(entity);
        });
        return result;
    }
}
