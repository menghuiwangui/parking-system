package com.example.campus_parking_system.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;      // 200成功, 401未登录, 403无权限, 500错误
    private String msg;
    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok() { return ok(null); }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}