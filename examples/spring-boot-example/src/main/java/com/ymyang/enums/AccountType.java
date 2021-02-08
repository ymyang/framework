package com.ymyang.enums;

import com.ymyang.framework.mybatisplus.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 账号类型
 */
public enum AccountType implements BaseEnum<Integer> {

    OA(1,"OA账号"),
    System(2,"系统账号");

    private Integer value;
    private String desc;

    AccountType(Integer value, String desc) {
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
