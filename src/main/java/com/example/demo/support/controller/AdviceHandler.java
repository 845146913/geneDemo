package com.example.demo.support.controller;

import com.example.demo.support.exception.BusinessException;
import com.example.demo.support.res.ResponseResult;
import com.example.demo.utils.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class AdviceHandler {

    @ExceptionHandler
    public Object exHandler(HttpServletRequest req, Exception ex) {
        String error = null;
        //对自定义的业务异常进行处理
        if(ex instanceof BusinessException) {
            error = ex.getMessage();
            errorLog("业务异常!", req, ex);
        } else  {
            error = "502网络异常";
            errorLog("系统异常", req, ex);
        }
        return ResponseResult.error(error);
    }

    private String errorLog(String title,HttpServletRequest req,Exception ex){
        String loginName = null;
        String requestParams= BeanMapper.objToJson(req.getParameterMap());
        StringBuffer errorLog = new StringBuffer(title+":")
                .append("\n 服务接口：").append(req.getRequestURL().toString())
                .append("\n 请求类型：").append(req.getMethod())
                .append("\n 登录用户：").append(loginName)
                .append("\n 请求时间：").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S").format(new Date()))
                .append("\n 请求数据：").append(requestParams)
                .append("\n 异常信息：").append(ex.getMessage());
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@ errorLog:{}", errorLog);
        return errorLog.toString();
    }
}
