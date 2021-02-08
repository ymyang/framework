package com.ymyang.framework.mybatisplus.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InnerJoins {

    InnerJoin[] value();

}
