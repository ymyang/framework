package com.ymyang.enums;

import com.ymyang.framework.mybatisplus.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 菜单类型
 */
public enum MenuType implements BaseEnum<Integer> {

    Navigation(1,"导航"),
    Menu(2,"菜单"),
    Button(3, "按钮");

    private Integer value;
    private String desc;

    MenuType(Integer value, String desc) {
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
