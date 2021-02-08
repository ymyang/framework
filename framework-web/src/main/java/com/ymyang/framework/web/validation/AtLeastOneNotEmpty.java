package com.ymyang.framework.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {AtLeastOneNotEmptyValidator.class})
public @interface AtLeastOneNotEmpty {

    // 可选不为空的属性数组
    String[] value();

    //如果校验不通过返回的提示信息
    String message() default "至少有一个属性不可为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
