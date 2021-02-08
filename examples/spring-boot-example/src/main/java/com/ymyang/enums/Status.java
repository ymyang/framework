package com.ymyang.enums;

import com.ymyang.framework.mybatisplus.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 状态
 */
public enum Status implements BaseEnum<Integer> {

    Enable(1,"启用"),
    Disable(2, "禁用");

    private Integer value;
    private String desc;

    Status(Integer value, String desc) {
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
