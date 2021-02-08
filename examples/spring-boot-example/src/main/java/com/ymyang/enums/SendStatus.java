package com.ymyang.enums;

import com.ymyang.framework.mybatisplus.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 发送状态
 */
public enum SendStatus implements BaseEnum<Integer> {

    UnSent(1,"未发送"),
    Success(2,"发送成功"),
    Failure(3, "发送失败");

    private Integer value;
    private String desc;

    SendStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
