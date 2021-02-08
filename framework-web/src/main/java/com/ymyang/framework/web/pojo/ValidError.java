package com.ymyang.framework.web.pojo;

import lombok.Data;

@Data
public class ValidError {

    private String field;

    private String code;

    private String message;

    private Object rejectedValue;
}
