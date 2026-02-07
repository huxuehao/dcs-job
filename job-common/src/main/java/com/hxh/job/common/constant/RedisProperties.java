package com.hxh.job.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    private Map<String, Object> sentinel;

    public String getAddress() {
        return "redis://" + this.host + ":" + this.port;
    }

    public String[] getClusterNodeAddress() {
        return getRedisHosts(this.cluster.get("nodes"));

    }
    public String[] getSentinelNodeAddress() {
        return getRedisHosts(this.sentinel.get("nodes"));
    }

    private String[] getRedisHosts(Object o) {
        if (o instanceof Collection<?>) {
            List<String> addressList = new ArrayList<>();
            for (Object item : (Collection<?>) o) {
                if (item instanceof String) {
                    addressList.add("redis://"+ item);
                } else {
                    addressList.add("redis://"+ item.toString());
                }
            }
            // 使用toArray(new String[0])来避免类型转换警告
            return addressList.toArray(new String[0]);
        } else if (o instanceof Map<?, ?>) {
            List<String> redisHosts = new ArrayList<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) o).entrySet()) {
                if (entry.getValue() instanceof String) {
                    redisHosts.add("redis://"+ entry.getValue());
                } else {
                    redisHosts.add("redis://"+ entry.getValue().toString());
                }
            }
            // 使用toArray(new String[0])来避免类型转换警告
            return redisHosts.toArray(new String[0]);
        } else {
            String[] result = o.toString().split(",");
            // 可能需要处理空字符串或去除前后空格
            for (int i = 0; i < result.length; i++) {
                result[i] = "redis://"+ (result[i].trim());
            }
            return result;
        }
    }
}
