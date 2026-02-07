package com.hxh.job.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：扫描定时任务相关参数
 *
 * @author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.scheduled-task.scan", ignoreUnknownFields = false)
public class ScanProperties {
    private String taskPackage = "com.hxh.job.task";
    private Boolean autoInsert = false;
    private String defaultCron = "-1";
}
