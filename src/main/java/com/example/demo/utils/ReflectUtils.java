package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public class ReflectUtils extends ReflectionUtils {

    private static ReflectUtils utils = new ReflectUtils();

    private ReflectUtils(){

    }
    public static ReflectUtils getInstance() {
        return utils;
    }
    /**
     * 获取超类的泛型参数类型
     * @param genericSuperclass 直接超类
     * @param ind
     * @param <T>
     * @return
     */
    public static  <T> Class<T> getClass(Type genericSuperclass, int ind) {
        if(genericSuperclass instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType)genericSuperclass;
            Type[] args = type.getActualTypeArguments();
            Type arg = args[ind];
            log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ args[{}]:value:{}", ind, arg);
            return (Class<T>) arg;
        }
        return null;
    }
}
