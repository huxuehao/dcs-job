package com.hxh.job.common.annotation;

import java.lang.annotation.*;

/**
 * 描述：定时任务方法注解
 *
 * @author huxuehao
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskPath {
    /* 定时任务名 */
    String name() default "";
    /* 描述 */
    String describe() default "暂无描述";
    /* path，必填 */
    String path();
    /* 执行策略 */
    String cron() default "-1";
    /* 分类 */
    String type() default "default";
    /* 是否启用 */
    int enable() default 0;
    /* 是否开启日志采集 */
    int openLog() default 1;
}
