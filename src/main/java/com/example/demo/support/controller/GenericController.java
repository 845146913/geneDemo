package com.example.demo.support.controller;

import com.example.demo.support.dto.GenericDTO;
import com.example.demo.support.entity.GenericEntity;
import com.example.demo.support.res.page.Order;
import com.example.demo.support.res.page.PageRequest;
import com.example.demo.support.service.IGenericService;
import com.example.demo.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
        test();
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ utils:{}", reflectUtils);
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        this.eClass = reflectUtils.getClass(genericSuperclass,0);
        this.dtoClass = reflectUtils.getClass(genericSuperclass,2);
        this.cdtoClass = reflectUtils.getClass(genericSuperclass,3);
        this.udtoClass = reflectUtils.getClass(genericSuperclass,4);
    }
    private ReflectUtils reflectUtils = ReflectUtils.getInstance();

    @PostConstruct
    private void init() {
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ init Controller: utils:{}", ReflectUtils.getInstance());
    }
    /**
     * 获取超类的泛型参数
     * @param ind
     * @param <T>
     * @return
     */
    private  <T> Class<T> getClass(int ind) {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType)genericSuperclass;
            Type[] args = type.getActualTypeArguments();
            Type arg = args[ind];
            log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ args[{}]: {}", ind, arg);
            return (Class<T>) arg;
        }
        return null;
    }
    private void test() {
        int i = 0;
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@ test method:{}", i);
    }

    @GetMapping("/list")
    protected PageInfo<E> findAll(PageRequest pageRequest, DTO dto) throws Exception {
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
        Example example = new Example(eClass);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",dto.getId()); // TODO 动态查询参数及条件拼装

        log.debug("@@@@@@@@@@@@@@@@@@@@@@ dto :{} \n pageRequest:{}", dto, pageRequest);
        PageInfo<E> result = new PageInfo<>(getService().findByExample(example));
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
