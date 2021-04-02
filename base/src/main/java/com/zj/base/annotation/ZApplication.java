package com.zj.base.annotation;

import jdk.nashorn.internal.objects.annotations.Constructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标识为一个服务，并注册到注册中心
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Bean
public @interface ZApplication {
    /**
     * 服务名称
     */
    @AliasFor("name")
    String value() default "";
    @AliasFor("value")
    String name() default "";
}
