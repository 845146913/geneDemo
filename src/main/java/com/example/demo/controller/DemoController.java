package com.example.demo.controller;

import com.example.demo.model.Demo;
import com.example.demo.service.IDemoService;
import com.example.demo.support.controller.GenericController;
import com.example.demo.support.service.GenericService;
import com.example.demo.support.service.IGenericService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController extends GenericController<Demo, Integer>{

    public final static String PATH = "/demo";

    @Resource
    private IDemoService demoService;

    public DemoController() {
        super(PATH);
    }

    @Override
    protected IGenericService<Demo, Integer> getService() {
        return demoService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "test";
    }
}
