package com.yang.blog.response;


import lombok.Data;


public enum ResonseState {
    SUCCESS(true, 20000, "操作成功"),
    FAILED(false, 40000, "操作失败"),
    LOGIN_FAILED(false, 20001, "登录失败"),
    GET_RESOURE_FAILED(false, 40001, "获取失败"),
    ERROR_403(false, 40004, "记录不存在"),
    ERROR_404(false, 40005, "禁止访问"),
    ERROR_503(false, 50004, "网关超时"),
    ERROR_504(false, 50005, "服务器故障");

    private int code;
    private String message;
    private boolean success;

    ResonseState(boolean success, int code, String message) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    ResonseState() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
