package com.tiger.job.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Annotation PassAuth
 * @Description 用于跳过权限认证
 * @Author StudiousTiger
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassAuth {
    boolean required() default true;
}
