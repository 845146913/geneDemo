package com.example.demo.support.entity;

import java.io.Serializable;

public abstract class GenericEntity<PK extends Serializable> implements Persistable<PK> {
}
