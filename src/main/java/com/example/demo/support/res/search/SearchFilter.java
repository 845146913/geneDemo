package com.example.demo.support.res.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索条件参数转换
 *  示例: tk.mapper只支持单表通用mapper,暂时不做多表级联查询条件
 * search_EQ__user.name:1 ===> user.name, EQ, 1
 */
@Slf4j
public class SearchFilter {
    public enum Operator {
        EQ, LIKE, GT, LT, GTE, LTE,IN,
        NEQ, NIN, PLIKE, ALIKE;
    }

    public static final String PREFIX = "search_";

    public String fieldName;
    public Object value;
    public Operator operator;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public static List<SearchFilter> parse(HttpServletRequest request){
        return parse(WebUtils.getParametersStartingWith(request, PREFIX));
    }
    /**
     * 参数转换
     * @param params
     * @return
     */
    public static List<SearchFilter> parse(Map<String, Object> params) {
        List<SearchFilter> maps = new ArrayList<>();
        params.forEach((k, v) -> {
            String[] names = StringUtils.split(k, "__");
            if (names.length != 2) {
                log.debug("{} :is not a valid search filter name  @@@@@@@ \n value:{}", k, v);
                throw new IllegalArgumentException(k + " is not a valid search filter name");
            }
            String fieldName = names[1];
            Operator operator = Operator.valueOf(names[0]);
            maps.add(new SearchFilter(fieldName, operator, v));
        });
        return maps;
    }
}
