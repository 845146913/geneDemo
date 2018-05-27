package com.example.demo.dto;

import com.example.demo.support.dto.GenericDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DemoDTO extends GenericDTO<Integer> {

    private Integer id;

    @NotNull(message = "姓名不能为空!")
    private String name;

    @Override
    public Integer getId() {
        return id;
    }
}
