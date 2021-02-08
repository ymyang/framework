package com.ymyang.framework.mybatisplus.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT) // 返回json数据给浏览器时格式化
public interface BaseEnum<T extends Serializable> extends IEnum<T> {

    String getDesc();

    /**
     * Model 自动类型转换必须包含此方法
     *
     * @param value
     * @return
     */
    default BaseEnum of(T value) {
        BaseEnum[] enumConstants = this.getClass().getEnumConstants();
        if (enumConstants == null) {
            return null;
        }
        for (BaseEnum item : enumConstants) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

    /**
     * of eq getByValue
     *
     * @param value
     * @return
     */
    default BaseEnum getByValue(T value) {

        return of(value);
    }

//    /**
//     * 获取当前类
//     * @return
//     */
//    default BaseEnum[] getCurrentEnums() {
//        String clazzName = new Object() {
//            public String getClassName() {
//                String clazzName = this.getClass().getName();
//                return clazzName.substring(0, clazzName.lastIndexOf('$'));
//            }
//        }.getClassName();
//        try {
//            Class<?> aClass = Class.forName(clazzName);
//            Method ofMethod = aClass.getMethod("of");
//            return (BaseEnum[]) ofMethod.invoke(null);
//        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            return null;
//        }
//    }

}
