package com.tiger.job.core.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RetryTemplate
 * @Description 定时任务重试执行模板
 * @Author huxuehao
 **/
@Component
public abstract class RetryTemplate {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private int retryCount; /* 重试次数 */
    private int sleepTime;  /* 尝试等待时间 */

    /* 设置重试次数 */
    public RetryTemplate setRetryCount(int retryCount) {
        if (retryCount < 0) {
            this.retryCount = 1;
            log.error("重试次数 retryCount 必须大于等于0");
        } else {
            this.retryCount = retryCount;
        }
        return this;
    }

    /* 设置重试间隔时间 */
    public RetryTemplate setSleepTime(int sleepTime) {
        if (sleepTime <= 0) {
            this.sleepTime = 10;
            log.error("重试间隔时间 sleepTime 必须大于0");
        } else {
            this.sleepTime = sleepTime;
        }
        return this;
    }

    /* 执行业务 */
    protected abstract Object doBiz() throws Throwable;

    /* retry 执行器*/
    public boolean executor() throws Throwable {
        for (int i = 0; i < retryCount + 1; i++) {
            if ((Boolean) doBiz()) {
                return true;
            }
            Thread.sleep(TimeUnit.SECONDS.toMillis(sleepTime));
        }
        return false;
    }
}
