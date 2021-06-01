package com.xxjy.jyyh.http;

public class Response<T> {

    private int    code;
    private String msg;
    private String message;
    private T      data;
    private boolean success;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(int errorCode) {
        this.code = errorCode;
    }

    public void setMsg(String errorMsg) {
        this.msg = errorMsg;
    }

    public void setData(T data) {
        this.data = data;
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
