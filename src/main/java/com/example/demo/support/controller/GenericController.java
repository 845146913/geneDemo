package com.example.demo.support.controller;

import com.example.demo.support.dto.GenericDTO;
import com.example.demo.support.entity.GenericEntity;
import com.example.demo.support.service.IGenericService;
import com.example.demo.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.print.Pageable;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

public abstract class GenericController<E extends GenericEntity<PK>, PK extends Serializable, DTO extends GenericDTO<PK>,
        CDTO extends GenericDTO<PK>, UDTO extends GenericDTO<PK>> {

    private Class<DTO> dtoClass;
    private Class<CDTO> cdtoClass;
    private Class<UDTO> udtoClass;
    // 映射路径
    private String path;
    protected abstract IGenericService<E, PK> getService();
    public GenericController(String path) {
        this.path = path;
        this.dtoClass = ReflectUtils.getClass(GenericController.class, 2);
        this.cdtoClass = ReflectUtils.getClass(GenericController.class, 3);
        this.udtoClass = ReflectUtils.getClass(GenericController.class, 4);
    }

    @GetMapping("/list")
    protected PageInfo<E> findAll(PageInfo pageable) throws Exception {
        int pageNum = (pageable == null) ? 1 : pageable.getPageNum();
        int pageSize = (pageable == null) ? 15 : pageable.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<E> result = new PageInfo<>(getService().findAll());
        return result;
    }

    protected void preList(List<E> list, PageInfo pageable) throws Exception {

    }

    @GetMapping(value = "/find/{id}")
    protected DTO find(@PathVariable("id") PK id) throws Exception {
        DTO dto = dtoClass.newInstance();
        prefind(dto, id);
        E entity = getService().findById(id);
        Function<E, DTO> copy = (en) -> {
            try {
                DTO d = dtoClass.newInstance();
                return d;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        };
        DTO apply = copy.apply(entity);

        return dto;
    }

    protected void prefind(DTO dto, PK id) throws Exception {

    }
}
