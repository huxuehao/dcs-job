package com.tiger.job.common.annotation;

import java.lang.annotation.*;

/**
 * 描述：定时任务类注解
 *
 * @author huxuehao
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SchedulerBean {
}
