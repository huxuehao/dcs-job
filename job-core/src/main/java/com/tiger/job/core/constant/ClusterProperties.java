package com.tiger.job.core.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName ClusterProperties
 * @Description 集群相关参数
 * @Author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.scheduled-task.cluster", ignoreUnknownFields = false)
public class ClusterProperties {
    private boolean open = false;
}
