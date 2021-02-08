package com.ymyang.enums;

import com.ymyang.framework.mybatisplus.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 组织类型
 */
public enum OrganizationType implements BaseEnum<Integer> {

    Group(1,"集团"),
    Company(2,"公司"),
    Department(3, "部门");

    private Integer value;
    private String desc;

    OrganizationType(Integer value, String desc) {
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
