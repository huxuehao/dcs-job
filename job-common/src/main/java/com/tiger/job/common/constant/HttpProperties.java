package com.tiger.job.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：httpUtils参数
 *
 * @author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tiger.http-util", ignoreUnknownFields = false)
public class HttpProperties {
    private int socketTimeout = 60000;
    private int connectTimeout = 30000;
    private int connectionRequestTimeout = 60000;
    private int maxTotal = 1500;
    private int defaultMaxPerRoute = 300;
}
