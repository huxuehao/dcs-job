package com.tiger.job.core.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName ScanProperties
 * @Description TODO
 * @Author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.scheduled-task.scan", ignoreUnknownFields = false)
public class ScanProperties {
    private String taskPackage = "com.tiger.job.task";
    private Boolean autoInsert = false;
    private String defaultCron = "-1";
}
