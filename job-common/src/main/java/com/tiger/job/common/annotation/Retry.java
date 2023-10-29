package com.tiger.job.common.annotation;

import java.lang.annotation.*;

/**
 * 描述：错误重试注解，在TaskExecutorImpl.execute中被用到了
 *
 * @author huxuehao
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
}
