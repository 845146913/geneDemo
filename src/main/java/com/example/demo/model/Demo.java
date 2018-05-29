package com.example.demo.model;

import com.example.demo.support.entity.GenericEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demo extends GenericEntity<Integer> {

    @Id // 必须配置主键,不然selectByPrimaryKey方法会报错
    private Integer id;

    private String name;

    private String remark;
}
