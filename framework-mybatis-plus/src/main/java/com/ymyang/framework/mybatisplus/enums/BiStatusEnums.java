package com.ymyang.framework.mybatisplus.enums;

/**
 * mybatisplus's entity中枚举使用示例
 */
public enum BiStatusEnums implements BaseEnum<Integer> {

    DISABLE(0, "禁用"),
    ENABLE(1, "启用");

    private Integer value;
    private String desc;


    BiStatusEnums(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
//    @JsonValue
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }


}
