package com.hxh.job.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：重试策略配置
 *
 * @author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.scheduled-task.retry", ignoreUnknownFields = false)
public class RetryProperties {
    private int count = 3;
    private int sleep = 5;
    private boolean open = false;
}
