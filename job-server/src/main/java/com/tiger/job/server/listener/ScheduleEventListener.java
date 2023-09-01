package com.tiger.job.server.listener;

import com.alibaba.fastjson2.JSON;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.tiger.job.common.constant.ChannelConstant.*;

/**
 * 描述：调度事件监听器
 *
 * @author huxuehao
 **/
@Component
public class ScheduleEventListener {

    private final StringRedisTemplate stringRedisTemplate;

    public ScheduleEventListener(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @EventListener
    public void listenerScheduleEvent(ScheduleEvent event) {
        switch (event.getType()) {
            case OPEN:
                /* 发布消息,通知本节点及其他节点开启定时 */
                stringRedisTemplate.convertAndSend(OPEN, JSON.toJSONString(event.getTaskDto()));
                break;
            case CLOSE:
                /* 发布消息，通知本节点及其他节点关闭定时 */
                stringRedisTemplate.convertAndSend(CLOSE, JSON.toJSONString(event.getTaskDto()));
                break;
            case DELETE:
                /* 发送消息，通知本节点及其他节点删除定时 */
                stringRedisTemplate.convertAndSend(DELETE, JSON.toJSONString(event.getTaskDto()));
                break;
            default:
                throw new RuntimeException("ChannelConstant匹配失败，请保证正确的参数传递");
        }
    }
}
