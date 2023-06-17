package com.tiger.job.common.annotation;

import java.lang.annotation.*;

/**
 * @Annotation  RetryTask
 * @Description 错误重试注解，在TaskExecutorImpl.execute中被用到了
 * @Author huxuehao
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
}
