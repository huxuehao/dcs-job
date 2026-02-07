package com.hxh.job.core.config;

import com.alibaba.fastjson2.JSON;
import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.common.constant.ChannelConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 描述：redis 的订阅配置
 *
 * @author huxuehao
 **/
@Configuration
public class RedisSubscribeConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<String, Consumer<ScheduledConfigEntity>> triggerMap;

    public RedisSubscribeConfig(@Qualifier("triggerMap") Map<String, Consumer<ScheduledConfigEntity>> triggerMap) {
        this.triggerMap = triggerMap;
    }

    /* 消息的监听容器 */
    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        ArrayList<Topic> topicList = new ArrayList<>(); /* 定义订阅通道的集合 */
        // topicList.add(new PatternTopic("qianmo-job:*")); /* 正则匹配 */
        topicList.add(new ChannelTopic(ChannelConstant.OPEN)); /* 全量匹配 */
        topicList.add(new ChannelTopic(ChannelConstant.CLOSE));
        topicList.add(new ChannelTopic(ChannelConstant.DELETE));
        container.addMessageListener(this.messageListener(), topicList); /* 添加监听者和主体集合 */
        return container;
    }

    /* redis频道（消息）的监听者（接收者）*/
    @Bean
    public MessageListener messageListener() {
        /* 接收消息, 并根据channel的名称进行任务调度触发器 */
        return (message, pattern) -> {
            String channel = new String(message.getChannel());
            log.info("接收到了节点的广播消息，channel为：{}", channel);
            String body = new String(message.getBody());
            /* ScheduleTaskDto 是 Consumer 的参数，所以将消息体转换成ScheduleTaskDto对象，并作为 Consumer 的参数 */
            triggerMap.get(channel).accept(JSON.parseObject(body, ScheduledConfigEntity.class));

        };
    }
}
