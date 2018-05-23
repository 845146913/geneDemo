package com.example.demo.dto;

import com.example.demo.support.dto.GenericDTO;
import lombok.Data;

@Data
public class DemoDTO extends GenericDTO<Integer> {

    private Integer id;

    private String name;

    @Override
    public Integer getId() {
        return id;
    }
}
