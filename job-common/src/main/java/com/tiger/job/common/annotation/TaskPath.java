package com.tiger.job.common.annotation;

import java.lang.annotation.*;

/**
 * @@interface TaskPath
 * @Description 定时任务方法注解
 * @Author huxuehao
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
    String type() default "other_task";
    /* 是否启用 */
    String enable() default "0";
    /* 是否开启日志采集 */
    String openLog() default "1";
}
