package com.example.demo.support.res.search;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 搜索条件参数转换
 * user.name__EQ:1 ===> user.name, EQ, 1
 */
public class SearchFilter {
    public enum Operator {
        EQ, LIKE, GT, LT, GTE, LTE,IN,
        NEQ, NIN;
    }

    public String fieldName;
    public Object value;
    public Operator operator;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    /**
     * 参数转换
     * @param params
     * @return
     */
    public static Map<String, SearchFilter> parse(Map<String, Object> params) {
        Map<String, SearchFilter> maps = new HashMap<>();
        params.forEach((k, v) -> {
            String[] names = StringUtils.split(k, "__");
            if (names.length != 2) {
                throw new IllegalArgumentException(k + " is not a valid search filter name");
            }
            String fieldName = names[0];
            Operator operator = Operator.valueOf(names[1]);
            maps.put(k, new SearchFilter(fieldName, operator, v));
        });
        return maps;
    }
}
