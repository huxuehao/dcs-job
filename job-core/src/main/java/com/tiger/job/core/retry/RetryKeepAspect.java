package com.tiger.job.core.retry;

import com.tiger.job.common.constant.RetryProperties;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.core.queue.TaskRetryQueue;
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
    private final RetryProperties retryProperties;
    private final TaskRetryQueue taskRetryQueue;
    private final RetryActuator retryActuator;
    public RetryKeepAspect(RetryProperties retryProperties, TaskRetryQueue taskRetryQueue, RetryActuator retryActuator) {
        this.retryProperties = retryProperties;
        this.taskRetryQueue = taskRetryQueue;
        this.retryActuator = retryActuator;
    }

    @Pointcut("@annotation(com.tiger.job.common.annotation.Retry)")
    public void retryPointcut() {

    }

    @Around("retryPointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 判断retry是否开启
        if (!retryProperties.isOpen()) {
            return joinPoint.proceed();
        }
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            if (args[0] instanceof ScheduleTaskDto) {
                try {
                    // 执行
                    Object res = joinPoint.proceed();
                    if (!((Boolean) res)) {
                        // 推送到重试队列
                        this.pushToRetryQueue(args[0]);
                        // 尝试启动重试执行器
                        retryActuator.tryStart();
                    } else {
                        // 尝试关闭重试执行器
                        retryActuator.tryStop();
                    }
                    return res;
                } catch (Throwable e) {
                    // 推送到重试队列
                    this.pushToRetryQueue(args[0]);
                    // 尝试启动重试执行器
                    retryActuator.tryStart();
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("@Retry 所标记的方法的参数类型不符合预期.");
            }
        } else {
            throw new RuntimeException("@Retry 所标记的方法的参数数量不符合预期.");
        }
    }

    /**
     * 将失败的任务推送到重试队列
     * @param scheduledConfig 任务配置
     */
    public void pushToRetryQueue(Object scheduledConfig) {
        String taskId = ((ScheduleTaskDto)scheduledConfig).getId();
        String taskTag = taskRetryQueue.getTagById(taskId);
        long nextTime = System.currentTimeMillis() + retryProperties.getSleep() * 1000L;
        // 成立条件：在队列中没有找到
        if (taskTag == null) {
            // 向队列中添加，并指定下次执行时间
            taskRetryQueue.add(taskRetryQueue.genTaskTag(taskId, 1), nextTime);
        }
        // 成立条件：队列中找到了，但是执行次数已经达到上限
        else if (taskRetryQueue.parseRetriedTimes(taskTag) >= retryProperties.getCount()) {
            // 移除队列中的任务
            taskRetryQueue.remove(taskTag);
        }
        // 成立条件：队列中找到了，但是执行次数未达到上限
        else {
            // 移除队列中的任务！
            taskRetryQueue.remove(taskTag);
            // 向队列中添加，并指定下次执行时间
            taskRetryQueue.add(taskRetryQueue.genTaskTag(taskId, taskRetryQueue.parseRetriedTimes(taskTag) +1), nextTime);
        }
    }
}
