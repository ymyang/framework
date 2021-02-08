package com.ymyang.framework.beans.exception;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {

    private Integer code = -9999;

    public BusinessException() {
        this("");
    }

    public BusinessException(Integer code) {
        this();
        this.code = code;
    }

    public BusinessException(String message) {
        // 仅做业务错误异常，不跟踪异常所在代码（优化异常性能）
        super(message, null, false, false);
    }

    public BusinessException(String message, Integer code) {
        this(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
