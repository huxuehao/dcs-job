package com.hxh.job.core.config;

import com.hxh.job.common.constant.RedisProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：Redisson的配置类，在TaskExecutorImpl中使用到了Redisson事务锁
 *
 * @author huxuehao
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
                    .addNodeAddress(redisProperties.getClusterNodeAddress());
            if (redisProperties.getPassword() != null && !"".equals(redisProperties.getPassword())) {
                clusterServersConfig.setPassword(redisProperties.getPassword());
            }
        }
        /* 哨兵模式 */
        else if(redisProperties.getSentinel() != null) {
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers()
                    .setMasterName((String)redisProperties.getSentinel().getOrDefault("master", "master"))
                    .addSentinelAddress(redisProperties.getSentinelNodeAddress())
                    .setCheckSentinelsList(false);
            if (redisProperties.getPassword() != null && !"".equals(redisProperties.getPassword())) {
                sentinelServersConfig.setPassword(redisProperties.getPassword());
            }
            sentinelServersConfig.setDatabase(redisProperties.getDatabase());
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
