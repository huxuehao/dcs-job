package com.tiger.job.common.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName CommonProperties
 * @Description httpUtils参数
 * @Author huxuehao
 **/
@Component
@ConfigurationProperties(prefix = "tiger.http-util", ignoreUnknownFields = false)
public class HttpProperties {
    private static int socketTimeout = 60000;
    private static int connectTimeout = 30000;
    private static int connectionRequestTimeout = 60000;
    private int maxTotal = 1000;
    private int defaultMaxPerRoute = 200;

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }
    public static int getSocketTimeout() {
        return socketTimeout;
    }

    public  void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public static int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public static int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }
}
