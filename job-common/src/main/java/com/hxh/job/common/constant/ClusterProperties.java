package com.hxh.job.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：集群相关参数
 *
 * @author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.scheduled-task.cluster", ignoreUnknownFields = false)
public class ClusterProperties {
    private boolean open = false;
}
