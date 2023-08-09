package com.tiger.job.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 连接池配置
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.scheduled-tasks.pool")
public class TaskPoolProperties {
    // cpu核数的倍数
    int processorTimes = 2;
    // 线程池上下浮动数
    int floatNumber = 0;
    // 等待终止时间
    int awaitTerminationSeconds = 30;
    // 调度器shutdown后，是否等待当前调度执行完成
    boolean completeOnShutdown = true;
    // 线程名称前缀
    String threadNamePrefix = "TaskThread:";
}
