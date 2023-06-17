package com.tiger.job.common.annotation;

import java.lang.annotation.*;

/**
 * @Annotation SchedulerBean
 * @Description 定时任务类注解
 * @Author huxuehao
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SchedulerBean {
}
