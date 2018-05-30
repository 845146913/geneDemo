package com.example.demo.support.res;

import com.example.demo.support.enumeration.GenericEnum;

public enum ResultCode implements GenericEnum<ResultCode, Integer>{
    OK(0),
    ERROR(100);
    private int value;
    ResultCode(int value) {
        this.value = value;
    }

    @Override
    public Integer value() {
        return value;
    }
}
