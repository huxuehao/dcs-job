package com.tiger.job.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 描述：动态的配置redis的信息
 *
 * @author huxuehao
 **/
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis", ignoreUnknownFields = true)
public class RedisProperties {
    private String host;
    private String port;
    private String password;
    private int database = 0;
    private Map<String, Object> cluster;

    public String getAddress() {
        return "redis://" + this.host + ":" + this.port;
    }

    public String[] getNodeAddress() {
        String[] redisHosts = this.cluster.get("nodes").toString().split(",");
        for (int i = 0; i < redisHosts.length; i++) {
            redisHosts[i] = "redis://"+redisHosts[i];
        }
        return redisHosts;
    }

}