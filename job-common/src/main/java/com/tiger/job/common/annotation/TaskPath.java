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
    String name() default "";
    String describe() default "暂无描述";
    String path() default "";
    String cron() default "-1";
    String type() default "other_task";
    String enable() default "0";
    String openLog() default "1";
}
