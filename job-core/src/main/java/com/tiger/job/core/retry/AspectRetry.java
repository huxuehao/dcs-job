package com.tiger.job.core.retry;

import com.tiger.job.common.annotation.Retry;
import com.tiger.job.core.constant.RetryProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName AspectRetry
 * @Description 定时任务执行器的重试切面
 * @Author huxuehao
 **/
@Aspect
@Order(-1) /* 该切面应当先于 @Transactional 执行 */
@Component
public class AspectRetry {
    @Autowired
    RetryProperties retryProperties;

    @Pointcut("@annotation(com.tiger.job.common.annotation.Retry)")
    public void RetryPointcut() {

    }

    @Around("RetryPointcut()")
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
