package com.tiger.job.core.config;

import com.tiger.job.common.constant.RedisProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName LockConfig
 * @Description Redisson的配置类，在TaskExecutorImpl中使用到了Redisson事务锁
 * @Author huxuehao
 **/
@Configuration
public class RedissonConfig {

    private final RedisProperties redisProperties;

    public RedissonConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        /* 集群模式 */
        if (redisProperties.getCluster() != null) {
            ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    .addNodeAddress(redisProperties.getNodeAddress());
            if (redisProperties.getPassword() != null && !"".equals(redisProperties.getPassword())) {
                clusterServersConfig.setPassword(redisProperties.getPassword());
            }
        }
        /* 单例模式 */
        else {
            SingleServerConfig singleServerConfig = config.useSingleServer()
                    .setAddress(redisProperties.getAddress());
            if (redisProperties.getPassword() != null && !"".equals(redisProperties.getPassword())) {
                singleServerConfig.setPassword(redisProperties.getPassword());
            }
            singleServerConfig.setDatabase(redisProperties.getDatabase());
        }
        config.setLockWatchdogTimeout(20000L); /* 锁的续期时间设置20秒，默认是30秒 */
        return Redisson.create(config);
    }

}
