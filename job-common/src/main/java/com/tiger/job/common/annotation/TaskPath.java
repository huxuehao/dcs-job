package com.tiger.job.common.annotation;

import java.lang.annotation.*;

/**
 * @@interface SchedulerAPI
 * @Description TODO
 * @Author huxuehao
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskPath {
    String path() default "";
}
