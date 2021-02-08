package com.ymyang.framework.mybatisplus.annotation;

import java.lang.annotation.*;

/**
 * 此注解用在mapper的方法中
 * <p>
 * 方法的返回值可以用entity和pojo，entity支持属性嵌套其他entity，pojo不支持。
 * 建议聚合查询用pojo，数据查询用entity。
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(InnerJoins.class)
public @interface InnerJoin {

    /**
     * 关联表的entity
     * 约定优于配置，构成的SQL语句中的表别名，根据主表和InnerJoin顺序：t1、t2、t3...
     * 在自定义返回字段时、where查询条件时、on查询条件使用时，尽量写全，如：表别名.字段名
     *
     * @return
     */
    Class<?> join();

    /**
     * 关联表的entity对应的Mapper
     *
     * @return
     */
    Class<?> mapper();

    /**
     * on条件
     *
     * @return
     */
    String on() default "";

    /**
     * 查询结果映射到返回类型中的属性名，多级属性用点隔开，但必须上级属性存在
     * <p>
     * 效果如：
     * <association property="${property}" columnPrefix="${property}"></association>
     * <collection property="${property}" columnPrefix="${property}"></collection>
     *
     * @return
     */
    String property() default "";

    /**
     * 默认所有字段
     * 优先级： Wrappers.query().select() > 此参数 > 默认（所有字段）
     * <p>
     * 如果多表join，只需在其中一个@LeftJoin中设置select()，但也可以在多个leftJoin中分开设置（推荐）
     * 如果返回字段需绑定到嵌套对象的属性中，字段别名的形式为：对象属性.[对象属性.]字段名
     * 如："t1.id, t2.id AS `property.id`"
     *
     * @return
     */
    String select() default "";

    /**
     * 排序（小的排在前）, 当InnerJoin和LeftJoin交替混用时，且有重复注解时使用，避免@Repeatable影响顺序
     * 当不存在InnerJoin和LeftJoin交替混用时，不需要设置额外设置，
     * <p>
     * 注意：排序对表别名息息相关
     *
     * @return
     */
    int order() default -128;

}
