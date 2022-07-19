package com.yang.blog.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ResponseResult {
    private boolean success;
    private int code;
    private String message;
    private Object data;

    public ResponseResult(boolean success, int code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseResult() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public Object getData() {
        return data;
    }


    public void setData(Object data) {
        this.data = data;
    }

    public ResponseResult(ResonseState resonseState) {
        this.success = resonseState.isSuccess();
        this.code = resonseState.getCode();
        this.message = resonseState.getMessage();
    }

    public static ResponseResult ERROR_403() {
        return new ResponseResult(ResonseState.ERROR_403);
    }

    public static ResponseResult SUCCESS() {
        return new ResponseResult(ResonseState.SUCCESS);
    }

    public static ResponseResult ERROR_404() {
        return new ResponseResult(ResonseState.ERROR_404);
    }

    public static ResponseResult ERROR_504() {
        return new ResponseResult(ResonseState.ERROR_504);
    }

    public static ResponseResult ERROR_505() {
        return new ResponseResult(ResonseState.SUCCESS);
    }

    public static ResponseResult SUCCESS(String message) {
        ResponseResult responseResult = new ResponseResult(ResonseState.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult FAILED() {
        return new ResponseResult(ResonseState.FAILED);
    }

    public static ResponseResult FAILED(String message) {
        ResponseResult responseResult = new ResponseResult(ResonseState.FAILED);
        responseResult.setMessage(message);
        return responseResult;
    }
}
