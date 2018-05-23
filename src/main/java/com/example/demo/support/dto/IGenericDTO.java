package com.example.demo.support.dto;

import java.io.Serializable;

public interface IGenericDTO<PK extends Serializable> extends Serializable {

    PK getId();
}
