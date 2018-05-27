package com.example.demo.controller;

import com.example.demo.dto.DemoDTO;
import com.example.demo.model.Demo;
import com.example.demo.service.IDemoService;
import com.example.demo.support.controller.GenericController;
import com.example.demo.support.exception.BusinessException;
import com.example.demo.support.service.IGenericService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(DemoController.PATH)
@Slf4j
public class DemoController extends GenericController<Demo, Integer, DemoDTO, DemoDTO, DemoDTO>{

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

    @GetMapping("/create")
    public DemoDTO create(@Valid DemoDTO dto, BindingResult bindingResult) throws  Exception {

        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@ dto:{}", dto);
        if(bindingResult.hasErrors()) {
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            allErrors.forEach( error -> {
                log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@2 model has error:{}, {}", error.getField(),  error.getDefaultMessage());
                throw new BusinessException(error.getDefaultMessage());
            });
        }
        return dto;
    }
}
