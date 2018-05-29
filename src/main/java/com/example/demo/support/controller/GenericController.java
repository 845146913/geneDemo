package com.example.demo.support.controller;

import com.example.demo.support.dto.GenericDTO;
import com.example.demo.support.entity.GenericEntity;
import com.example.demo.support.exception.BusinessException;
import com.example.demo.support.res.ResponseResult;
import com.example.demo.support.res.page.Order;
import com.example.demo.support.res.page.PageRequest;
import com.example.demo.support.res.search.DynamicSpecifications;
import com.example.demo.support.res.search.SearchFilter;
import com.example.demo.support.service.IGenericService;
import com.example.demo.utils.BeanMapper;
import com.example.demo.utils.ReflectUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class GenericController<E extends GenericEntity<PK>, PK extends Serializable, DTO extends GenericDTO<PK>,
        CDTO extends GenericDTO<PK>, UDTO extends GenericDTO<PK>> {

    private Class<DTO> dtoClass;
    private Class<E> eClass;
    private Class<CDTO> cdtoClass;
    private Class<UDTO> udtoClass;

    // 映射路径
    private String path;
    protected abstract IGenericService<E, PK> getService();

    /**
     * java类的初始化顺序是
     * 1.执行构造函数(里面有变量从上而下执行)
     * 2.如果加了@PostConstruct注解，后初始化
     * @param path
     */
    public GenericController(String path) {
        this.path = path;
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ utils:{}", reflectUtils);
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        this.eClass = reflectUtils.getClass(genericSuperclass,0);
        this.dtoClass = reflectUtils.getClass(genericSuperclass,2);
        this.cdtoClass = reflectUtils.getClass(genericSuperclass,3);
        this.udtoClass = reflectUtils.getClass(genericSuperclass,4);
    }
    private ReflectUtils reflectUtils = ReflectUtils.getInstance();

    @PostConstruct
    private void init() {
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ init Controller: utils:{}", ReflectUtils.getInstance());
    }

    /**
     * 列表
     * @param pageRequest
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    protected PageInfo<E> list(PageRequest pageRequest, HttpServletRequest request) throws Exception {
        int pageNum = pageRequest.getPageNum() == null ? 1 : pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize() == null ? 20 : pageRequest.getPageSize();

        PageHelper.startPage(pageNum, pageSize);

        String order = pageRequest.getOrder(); // asc OR desc
        String sort = pageRequest.getSort();  // 排序
        if(StringUtils.isEmpty(sort)) {
            pageRequest.setOpenSort(false);
        } else {
            pageRequest.setOpenSort(true);
            pageRequest.setAsc(Objects.equals(Order.ASC.value(), order) ? true : false);
        }
        if (pageRequest.isOpenSort()) {
            PageHelper.orderBy(sort + " " + order);
        }
        // TODO 动态查询参数及条件拼装
        List<SearchFilter> filters = SearchFilter.parse(request);
        preList(pageRequest, request, filters);
        Example example = DynamicSpecifications.bySearchFilter(eClass, filters);

        log.debug("@@@@@@@@@@@@@@@@@@@@@@ params :{} \n pageRequest:{}", SearchFilter.parse(request), pageRequest);
        PageInfo<E> result = new PageInfo<>(getService().findByExample(example));
        return result;
    }

    protected void preList(PageRequest pageRequest, HttpServletRequest request, Collection<SearchFilter> filters) throws Exception {

    }

    @GetMapping(value = "/find/{id}")
    protected DTO find(@PathVariable("id") PK id) throws Exception {
        DTO dto = dtoClass.newInstance();
        E entity = getService().findById(id);
        BeanUtils.copyProperties(entity, dto);
        postfind(dto, id);
        return dto;
    }

    protected void postfind(DTO dto, PK id) throws Exception {

    }

    @PostMapping("/create")
    protected ResponseResult<Object> create(@Valid CDTO dto, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        if(bindingResult.hasErrors()) {
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            allErrors.forEach( error -> {
                log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@2 create model has error:{} :  {}",  error.getField(),  error.getDefaultMessage());
                throw new BusinessException(error.getDefaultMessage());
            });
        }
        E e = eClass.newInstance();
        postSave(dto, bindingResult);
        BeanUtils.copyProperties(dto, e);
        e = getService().save(e);
        return e == null ?ResponseResult.error("新增失败"): ResponseResult.ok(e, "新增成功");
    }

    protected void postSave(CDTO dto, BindingResult bindingResult) throws Exception{

    }

    @PostMapping("/update/{id}")
    protected ResponseResult<Object> update(@Valid UDTO dto, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        if(bindingResult.hasErrors()) {
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            allErrors.forEach( error -> {
                log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@2 update model has error:{} :  {}",  error.getField(),  error.getDefaultMessage());
                throw new BusinessException(error.getDefaultMessage());
            });
        }
        E e = eClass.newInstance();
        postUpdate(dto, bindingResult);
        BeanUtils.copyProperties(dto, e);
        e = getService().update(e);
        return e == null ?ResponseResult.error("修改失败"): ResponseResult.ok(e, "修改成功");
    }

    protected  void postUpdate(UDTO dto, BindingResult bindingResult) throws Exception{

    }

    @PostMapping("/batchdel")
    protected ResponseResult<Object> delete(@RequestParam("ids") List<String> ids, HttpServletRequest request) throws Exception{
        String idStr = ids.stream().collect(Collectors.joining(","));
        return getService().delete(idStr) > 0 ? ResponseResult.ok(null, "删除成功!") :ResponseResult.error("删除失败!");
    }


}
