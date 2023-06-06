package com.tiger.job.core.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName RetryProperties
 * @Description 重试策略配置
 * @Author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.scheduled-task.retry", ignoreUnknownFields = false)
public class RetryProperties {
    private int count = 3;
    private int sleep = 5;
    private boolean open = false;
}
