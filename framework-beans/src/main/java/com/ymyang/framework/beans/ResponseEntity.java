package com.ymyang.framework.beans;


import lombok.Data;

import java.io.Serializable;

/**
 * 返回数据
 */
@Data
public class ResponseEntity<T> implements Serializable {

    /**
     * 服务状态, 0:成功，其他：异常
     */
    private int code = 0;

    /**
     * 服务说明
     */
    private String message = "";

    /**
     * 返回数据
     */
    private T data;

    public ResponseEntity() {

    }

    /**
     * @param code
     * @param message
     * @param data
     */
    public ResponseEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @param code
     * @param message
     */
    public ResponseEntity(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @param data
     */
    public ResponseEntity(T data) {
        this.data = data;
    }

    /**
     * @param data
     * @param message
     */
    public ResponseEntity(T data, String message) {
        this.data = data;
        this.message = message;
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> success() {
        return new ResponseEntity<>(0, "success", null);
    }

    /**
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> success(String message) {
        return new ResponseEntity<>(0, message, null);
    }

    /**
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(data);
    }

    /**
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> success(T data, String message) {
        return new ResponseEntity<>(data, message);
    }

    /**
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> error(String message) {
        return new ResponseEntity<>(-21, message, null);
    }

    /**
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> error(int code, String message) {
        return new ResponseEntity<>(code, message, null);
    }

    /**
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> error(String message, T data) {
        return new ResponseEntity<>(-21, message, data);
    }

}
