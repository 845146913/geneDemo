package com.example.demo.support.res;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult<T> implements Serializable {

    /**
     * 响应码
     */
    private Object code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T  data;

    private static <T> ResponseResult<T> response(Object code, T data, String msg) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    public static <T> ResponseResult<T> ok(T data, String msg) {
        return response(ResultCode.OK.value(), data, msg);
    }
    public static <T> ResponseResult<T> ok(T data) {
        return ok(data, null);
    }
    public static <T> ResponseResult<T> error(Object code, String msg) {
        return response(code, null, msg);
    }
    public static <T> ResponseResult<T> error(String msg) {
        return error(ResultCode.ERROR.value(), msg);
    }

}
