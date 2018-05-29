package com.example.demo.support.service;

import com.example.demo.support.GenericRepository;
import com.example.demo.support.entity.GenericEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 通用抽象service
 * @param <E>
 * @param <PK>
 */
@Slf4j
public abstract class GenericService<E extends GenericEntity<PK>, PK  extends Serializable> implements IGenericService<E,PK>{



    protected Class<E> tClass;


    protected abstract GenericRepository<E, PK> getRepository();
    public GenericService() {
        Type type = getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) type;
            tClass = (Class<E>) t.getActualTypeArguments()[0];
            log.debug("genericService init entityClass:{}", tClass);
        }
    }

    @Override
    public List<E> findAll() {
        return getRepository().selectAll();

    }

    @Override
    public List<E> findByExample(Example o) {
        return getRepository().selectByExample(o);
    }

    @Override
    public E findById(PK id) {
        return getRepository().selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public E save(E entity) {
        Objects.requireNonNull(entity);
        int result = getRepository().insert(entity);
        return result > 0 ? entity : null;
    }

    @Override
    @Transactional
    public <S extends E> List<S> batchSave(Iterable<S> entities) {
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

    @Override
    public E update(E e) {
        return getRepository().updateByPrimaryKey(e) > 0 ? e : null;
    }

    @Override
    public int delete(String ids) {
        int i = getRepository().deleteByIds(ids);
        return i;
    }
}
