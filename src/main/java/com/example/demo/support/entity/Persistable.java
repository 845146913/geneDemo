package com.example.demo.support.entity;

import java.io.Serializable;

public interface Persistable<PK extends Serializable> extends Serializable{

    PK getId();
}
