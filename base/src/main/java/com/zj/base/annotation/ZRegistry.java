package com.zj.base.annotation;

import java.lang.annotation.*;

/**
 * 将这添加到spring容器，并且标识一个注册中心
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZRegistry {
    String value() default "";
}
