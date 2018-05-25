package com.example.demo.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtils extends ReflectionUtils {

    /**
     * 获取超类的泛型参数
     * @param tClass
     * @param ind
     * @param <T>
     * @return
     */
    public static  <T> Class<T> getClass(Class<?> tClass, int ind) {
        Type genericSuperclass = tClass.getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType)genericSuperclass;
            Type[] args = type.getActualTypeArguments();
            Type arg = args[ind];
            if(null != arg && arg instanceof ParameterizedType) {
                return (Class<T>) arg;
            }
        }
        return null;
    }
}
