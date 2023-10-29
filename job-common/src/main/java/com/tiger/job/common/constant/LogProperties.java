package com.tiger.job.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：日志配置文件
 *
 * @author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.scheduled-task.log", ignoreUnknownFields = false)
public class LogProperties {
    private boolean successOpen = true; /* 启用操作成功日志 */
    private boolean failOpen = true; /* 启用操作失败日志 */
    private int saveDays = 2; /* 日志保存天数 */
}
