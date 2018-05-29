package com.example.demo.support.res.search;

import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Collection;

public class DynamicSpecifications {

    public static <T> Example bySearchFilter(Class<T> tClass, final Collection<SearchFilter> filters) {
        Example example = new Example(tClass);
        Example.Criteria criteria = example.createCriteria();
        filters.forEach(searchFilter -> {
            String fieldName = searchFilter.fieldName;
            SearchFilter.Operator operator = searchFilter.operator;
            Object value = searchFilter.value;
            switch (operator) {
                case EQ:
                    criteria.andEqualTo(fieldName, value);break;
                case IN:
                    criteria.andIn(fieldName, Arrays.asList(value));break;
                case NEQ:
                    criteria.andNotEqualTo(fieldName, value);break;
                case GT:
                    criteria.andGreaterThan(fieldName, value);break;
                case LT:
                    criteria.andLessThan(fieldName, value);break;
                case GTE:
                    criteria.andGreaterThanOrEqualTo(fieldName, value);break;
                case LTE:
                    criteria.andLessThanOrEqualTo(fieldName, value);break;
                case NIN:
                    criteria.andNotIn(fieldName, Arrays.asList(value));break;
                case LIKE:
                    criteria.andLike(fieldName, "%" + value + "%");break;
                case ALIKE:
                    criteria.andLike(fieldName, value + "%");break;
                case PLIKE:
                    criteria.andLike(fieldName, "%" + value);break;

                default:
                    break;
            }
        });
        return example;
    }
}
