package com.hxh.job.core.retry;

import com.hxh.job.common.entity.ScheduledConfigEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 重试拦截器
 * 用于对需要重试任务的队列维护
 *
 * @author huxuehao
 **/
@Aspect
@Order(-1)
/* 该切面应当先于 @Transactional 执行 */
@Component
/* 判断retry是否开启 */
@ConditionalOnProperty(name="tiger.scheduled-task.retry.open", havingValue = "true", matchIfMissing = false)
public class RetryKeepAspect {
    private final RetryActuator retryActuator;
    public RetryKeepAspect(RetryActuator retryActuator) {
        this.retryActuator = retryActuator;
    }

    @Pointcut("@annotation(com.hxh.job.common.annotation.Retry)")
    public void retryPointcut() {

    }

    @Around("retryPointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            if (args[0] instanceof ScheduledConfigEntity) {
                // 执行
                Object res = joinPoint.proceed();
                // 若执行失败推送到重试队列
                if (Boolean.FALSE.equals(res)) retryActuator.pushToRetryQueue(args[0]);
                return res;
            } else {
                throw new RuntimeException("@Retry 所标记的方法的参数类型不符合预期");
            }
        } else {
            throw new RuntimeException("@Retry 所标记的方法的参数数量不符合预期");
        }
    }
}
