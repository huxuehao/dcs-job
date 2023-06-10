package com.tiger.job.core.schedule;

import com.tiger.job.core.constant.ClusterProperties;
import com.tiger.job.core.constant.JobConstant;
import com.tiger.job.core.queue.TaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName HeartbeatSchedule
 * @Description 心跳检检测
 * @Author huxuehao
 **/
@Component
public class HeartbeatSchedule {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String TASK_HEART_BEAT_PREFIX = JobConstant.HEARTBEAT_PREFIX + JobConstant.LINK_TAG;

    @Autowired
    TaskQueue taskQueue;
    @Autowired
    @Qualifier("uniqueIdentifier")
    String uniqueIdentifier;
    @Autowired
    ClusterProperties cluster;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /* 获取心跳ID */
    private String getHeartbeatId() {
        return TASK_HEART_BEAT_PREFIX + uniqueIdentifier;
    }

    /* 发送心跳，心跳时长设置1分30秒 */
    @Scheduled(cron = "30 0/1 * * * ?")
    public void sendHeartbeat() {
        if (!cluster.isOpen()) {
            return;
        }
        try {
            /* 判断心跳是否存在 */
            if (redisTemplate.hasKey(getHeartbeatId())) {
                return;
            } else {
                redisTemplate.opsForValue().set(getHeartbeatId(), "1", 1000*90, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.error("[error]心跳检测：尝试发送心跳失败");
        }
    }

    /* 维持心跳，心跳时长设置1分30秒 */
    @Scheduled(cron = "15 0/1 * * * ?")
    public void keepHeartbeat() {
        if (!cluster.isOpen()) {
            return;
        }
        try {
            /* 判断心跳是否存在 */
            if (redisTemplate.hasKey(getHeartbeatId())) {
                redisTemplate.expire(getHeartbeatId(), 1000*90, TimeUnit.MILLISECONDS);
            } else {
                redisTemplate.opsForValue().set(getHeartbeatId(), "1", 1000*90, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.error("[error]心跳检测：尝试维持心跳失败");
        }
    }

    /* 清除队列中不合法的元素*/
    @Scheduled(cron = "45 0/1 * * * ?")
    public void clearIllegalQueueItem() {
        if (!cluster.isOpen()) {
            return;
        }
        try {
            // 获取redis中所有合法的心跳
            Set<String> legalHeartbeat = redisTemplate.keys(TASK_HEART_BEAT_PREFIX.concat("*"));
            // 获取所有的队列（不要查库，最好在bean的存储）
            List<String> allQueue = taskQueue.getKeys();
            /* 无队列时*/
            if (allQueue == null || allQueue.size() == 0) {
                return;
            }
            /* 无合法心跳时*/
            if (legalHeartbeat == null || legalHeartbeat.size() == 0) {
                return;
            }
            // 去除每个队列中非法合法心跳的元素
            List<String> legalHeartbeatList = new ArrayList<>(legalHeartbeat);
            for (String queueName : allQueue) {
                List<String> allItems = taskQueue.getAll(queueName);
                List<String> queueItemsPrefix = queueItemMoreHeartbeat(allItems, legalHeartbeatList);
                for (String itemsPrefix : queueItemsPrefix) {
                    taskQueue.remove(queueName, itemsPrefix);
                }
            }
        } catch (Exception e) {
            log.error("[error]非法队列：尝试清除非法队列元素失败");
        }
    }

    /**
     * 获取队列中元素不在心跳集合中的元素前缀
     * @param queueItem 队列元素集合
     * @param heartbeat 心跳集合
     * @return 待删除元素前缀集合
     */
    private List<String> queueItemMoreHeartbeat(List<String> queueItem, List<String> heartbeat) {
        List<String> heartbeats = heartbeat.stream().map(item -> item.split(JobConstant.LINK_TAG)[1]).collect(Collectors.toList());
        /* 获取队列中元素不在心跳集合中的元素前缀 */
        List<String> queueItemsPrefix = new ArrayList<>();
        for (String s : queueItem) {
            if (!heartbeats.contains(s)) {
                queueItemsPrefix.add(s);
            }
        }
        return queueItemsPrefix;
    }
}
