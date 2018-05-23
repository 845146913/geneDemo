package com.example.demo.model;

import com.example.demo.support.entity.GenericEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demo extends GenericEntity<Integer> {

    private Integer id;

    private String name;

    private String remark;
    @Override
    public Integer getId() {
        return id;
    }
}
