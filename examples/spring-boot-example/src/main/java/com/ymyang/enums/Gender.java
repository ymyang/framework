package com.ymyang.enums;

import com.ymyang.framework.mybatisplus.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 性别
 */
public enum Gender implements BaseEnum<Integer> {

    Unknown(0,"未知"),
    Male(1,"男"),
    Female(2, "女");

    private Integer value;
    private String desc;

    Gender(Integer value, String desc) {
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
