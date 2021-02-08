package com.ymyang.framework.mybatisplus.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {

    /**
     * 外键
     *
     * @return
     */
    String value();

    /**
     * 主表的主键
     *
     * @return
     */
    String localKey() default "id";

    /**
     * 关联model class
     *
     * @return
     */
    Class relation();

}
