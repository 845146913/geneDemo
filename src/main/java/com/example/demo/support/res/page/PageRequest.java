package com.example.demo.support.res.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用分页查询条件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest implements Serializable{
    private Integer pageNum;
    private Integer pageSize;
    private String  sort;
    private String  order;
    private boolean openSort;
    private boolean asc;
}
