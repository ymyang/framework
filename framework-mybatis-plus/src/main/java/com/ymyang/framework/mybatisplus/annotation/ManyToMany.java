package com.ymyang.framework.mybatisplus.annotation;

import java.lang.annotation.*;

/**
 * 实现原理，pivotModel t1 LEFT JOIN relationModel t2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {

    /**
     * 主表的主键（数据库的字段名，不是类属性名）
     *
     * @return
     */
    String localKey() default "id";

    /**
     * 关联表主键（数据库的字段名，不是类属性名）
     *
     * @return
     */
    String value() default "id";

    /**
     * 中间表
     *
     * @return
     */
    Class pivotModel();

    /**
     * 中间表中主表的外键（数据库的字段名，不是类属性名）
     *
     * @return
     */
    String pivotLocalKey();

    /**
     * 中间表中relation关联表外键（数据库的字段名，不是类属性名）
     *
     * @return
     */
    String pivotForeignKey();

    /**
     * 中间表中需查找字段（数据库的字段名，不是类属性名）
     *
     * @return
     */
    String[] pivotColumns() default {};

    /**
     * 关联model class
     *
     * @return
     */
    Class relationModel();

    /**
     * 返回的数据类型
     * 重要：resultType类中必须含有属性名为 pivotLocalKey()，否则查询出来，关联不到数据。
     *
     * @return
     */
    Class resultType();

    /**
     * 查询条件
     *
     * @return
     */
    String condition() default "";

}
