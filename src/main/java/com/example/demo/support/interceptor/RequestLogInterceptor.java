package com.example.demo.support.interceptor;

import com.example.demo.utils.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 请求日志拦截
 */
@Slf4j
@Component
public class RequestLogInterceptor extends HandlerInterceptorAdapter {

    /**
     * 调用之前参数
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestParams= BeanMapper.objToJson(request.getParameterMap());
        StringBuffer reqLog = new StringBuffer("")
                .append("\n 服务接口：").append(request.getRequestURL().toString())
                .append("\n 请求类型：").append(request.getMethod())
                .append("\n 请求时间：").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S").format(new Date()))
                .append("\n 请求数据：").append(requestParams);
        log.info("@@@@@@@@@@@@@@@@@@@ request:{}", reqLog);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String returnDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S").format(new Date());
        log.info("@@@@@@@@@@@@@@@@@@@ response: \n 服务接口：{} \n 返回状态：{}, \n 返回时间：{} \n 返回数据：{}",
                request.getRequestURL(), response.getStatus(),returnDate, handler);
        super.postHandle(request, response, handler, modelAndView);
    }
}
