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
                    criteria.andEqualTo(fieldName, value);
                case IN:
                    criteria.andIn(fieldName, Arrays.asList(value));
                case NEQ:
                    criteria.andNotEqualTo(fieldName, value);
                case GT:
                    criteria.andGreaterThan(fieldName, value);
                case LT:
                    criteria.andLessThan(fieldName, value);
                case GTE:
                    criteria.andGreaterThanOrEqualTo(fieldName, value);
                case LTE:
                    criteria.andLessThanOrEqualTo(fieldName, value);
                case NIN:
                    criteria.andNotIn(fieldName, Arrays.asList(value));
                case LIKE:
                    criteria.andLike(fieldName, "%" + value + "%");
            }
        });
        return example;
    }
}
