package com.ymyang.framework.web.shiro;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ShiroConfiguration.class)
@Documented
public @interface EnableShiroJWT {
}
