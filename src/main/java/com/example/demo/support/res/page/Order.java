package com.example.demo.support.res.page;

import com.example.demo.support.enumeration.GenericEnum;

public enum Order implements GenericEnum<Order> {
    ASC("asc"),
    DESC("desc");

    private String value;
    Order(String value) {
        this.value = value;
    }
    @Override
    public Object value() {
        return value;
    }
}
