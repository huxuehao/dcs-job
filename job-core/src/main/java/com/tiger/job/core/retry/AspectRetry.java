package com.tiger.job.core.retry;

import com.tiger.job.common.constant.RetryProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName AspectRetry
 * @Description 重试拦截器，定时任务执行器的重试切面
 * @Author huxuehao
 **/
@Aspect
@Order(-1) /* 该切面应当先于 @Transactional 执行 */
@Component
public class AspectRetry {
    private final RetryProperties retryProperties;
    public AspectRetry(RetryProperties retryProperties) {
        this.retryProperties = retryProperties;
    }

    @Pointcut("@annotation(com.tiger.job.common.annotation.Retry)")
    public void retryPointcut() {

    }

    @Around("retryPointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        if (retryProperties.isOpen()) {
            /* 设置重试策略 */
            return new RetryTemplate() {
                @Override
                protected Object doBiz() throws Throwable {
                    return joinPoint.proceed();
                }
            }.setRetryCount(retryProperties.getCount()).setSleepTime(retryProperties.getSleep()).executor();
        }
        return joinPoint.proceed();
    }
}
