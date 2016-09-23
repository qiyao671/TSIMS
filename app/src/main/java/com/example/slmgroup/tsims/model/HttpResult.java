package com.example.slmgroup.tsims.model;

/**
 * Created by lvqiyao (amorfatilay@163.com).
 * 2016/9/19 20:23.
 * 类描述：
 */
public class HttpResult<T> {
    public static final int CODE_SUCCEED = 1;
    public static final int CODE_FAILED = 0;
    int code;
    T data;

    public HttpResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
