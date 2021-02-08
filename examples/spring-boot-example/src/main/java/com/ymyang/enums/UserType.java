package com.ymyang.enums;

import com.ymyang.framework.mybatisplus.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户类型
 */
public enum UserType implements BaseEnum<Integer> {

    System(1,"系统管理员"),
    Normal(2,"普通用户");

    private Integer value;
    private String desc;

    UserType(Integer value, String desc) {
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
