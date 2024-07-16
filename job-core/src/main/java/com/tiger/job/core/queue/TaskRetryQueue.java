package com.tiger.job.core.queue;

import com.tiger.job.common.constant.JobConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 重试任务队列
 *
 * @author huxuehao
 **/
@Component
public class TaskRetryQueue {
    private static final String QUEUE_PREFIX = JobConstant.CLUSTER_RETRY_QUEUE_PREFIX + JobConstant.LINK_TAG;
    private final RedisTemplate<String, String> redisTemplate;

    public TaskRetryQueue(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 向队列中推入元素
     * @param taskTag  待重试执行任务标识
     * @param nextTime 下次执行时间
     */
    public void add(String taskTag, Long nextTime) {
        try {
            redisTemplate.opsForZSet().add(QUEUE_PREFIX, taskTag, nextTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除队列中的任务
     * @param taskTag 任务标识
     */
    public void remove(String taskTag) {
        try {
            redisTemplate.opsForZSet().remove(QUEUE_PREFIX, taskTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前时间需要执行的任务
     */
    public Set<String> getNow() {
        return this.get(System.currentTimeMillis());
    }

    /**
     * 获取当前时间需要执行的任务
     */
    public String getTagById(String taskId) {
        Set<String> tags = this.get(System.currentTimeMillis());
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return tags.stream().filter(item -> item.startsWith(taskId)).findFirst().orElse(null);
    }

    /**
     * 获取指定时间需要执行的任务
     * @param endTime 截止时间
     */
    public Set<String> get(Long endTime) {
        return this.get(0L, endTime);
    }

    /**
     * 获取指定时间需要执行的任务
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public Set<String> get(Long startTime, Long endTime) {
        return redisTemplate.opsForZSet().rangeByScore(QUEUE_PREFIX,startTime,endTime,0,1);
    }

    /**
     * 生成任务标记
     * @param taskId 任务ID
     * @param times  次数
     */
    public String genTaskTag(String taskId, int times) {
        return taskId + JobConstant.LINK_TAG + times;
    }

    /**
     * 解析已经重试的次数
     * @param taskTag 任务标记
     */
    public int parseRetriedTimes(String taskTag) {
        String[] split = taskTag.split(JobConstant.LINK_TAG);
        if (split.length != 2) {
            throw new RuntimeException("从重试队列中解析执行次数失败");
        }
        return Integer.parseInt(split[1]);
    }
    /**
     * 解析TaskId
     * @param taskTag 任务标记
     */
    public String parseTaskId(String taskTag) {
        String[] split = taskTag.split(JobConstant.LINK_TAG);
        if (split.length != 2) {
            throw new RuntimeException("从重试队列中解析TaskId失败");
        }
        return split[0];
    }
}
