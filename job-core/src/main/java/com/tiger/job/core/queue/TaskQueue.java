package com.tiger.job.core.queue;

import com.tiger.job.common.constant.JobConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TaskQueue
 * @Description 定时任务队列
 * @Author huxuehao
 **/
@Component
public class TaskQueue {

    private static final String QUEUE_PREFIX = JobConstant.CLUSTER_QUEUE_PREFIX + JobConstant.LINK_TAG;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getQueueLockName(String queueName) {
        return JobConstant.CLUSTER_QUEUE_LOCK_PREFIX + JobConstant.LINK_TAG + queueName;
    }

    public String getQueueName(String queueName) {
        return QUEUE_PREFIX + queueName;
    }

    public String getQueuePrefix() {
        return QUEUE_PREFIX;
    }
    /**
     * 查看队首元素
     * @param queueName 队列名
     * @return 结果
     */
    public String peek(String queueName) {
        try {
            return redisTemplate.opsForList().index(queueName, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向队列中推入元素
     * @param queueName 队列名
     * @param item      入队元素
     */
    public void push(String queueName, String item) {
        try {
            redisTemplate.opsForList().rightPush(queueName, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向队列中推入全部元素
     * @param queueName 队列名
     * @param items     入队元素集合
     * @return 结果
     */
    public boolean pushAll(String queueName, List<String> items) {
        try {
            redisTemplate.opsForList().rightPushAll(queueName, items);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 从队列中弹出元素
     * @param queueName 队列名
     */
    public void pop(String queueName){
        try {
            redisTemplate.opsForList().leftPop(queueName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从队列中弹出指定数量的元素
     * @param queueName 队列名
     * @param count     出队个数
     * @return          出队元素
     */
    public List<String> pop(String queueName, int count){
        try {
            return redisTemplate.opsForList().leftPop(queueName, count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据索引获取队列中的指定元素
     * @param queueName Object
     * @param index     下标索引（0为首首索引）
     * @return 索引对应的元素
     */
    public String index(String queueName, int index) {
        try {
            return redisTemplate.opsForList().index(queueName, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 判断队列是否存储
     * @param queueName 队列名
     * @return 结果
     */
    public boolean existQueue(String queueName) {
        try {
            Boolean aBoolean = redisTemplate.hasKey(queueName);
            return aBoolean == null ? false : aBoolean;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断队列中是否存在某一个元素
     * @param queueName 队列名
     * @param item      待判断元素
     * @return 结果
     */
    public boolean exist(String queueName, String item) {
        try {
            List<String> range = redisTemplate.opsForList().range(queueName, 0, -1);
            if (range != null) return false;
            for (String s : range) {
                if (s.startsWith(item)) return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取队列中的所有元素
     * @param queueName 队列名
     * @return 结果
     */
    public List<String> getAll(String queueName) {
        try {
            return redisTemplate.opsForList().range(queueName, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<String> getKeys() {
        try {
            Set<String> keys = redisTemplate.keys(QUEUE_PREFIX.concat("*"));
            if (keys == null) {
                return new ArrayList<>();
            } else {
                return new ArrayList<>(keys);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除队列的元素
     * @param queueName 队列名
     * @param itemPrefix 待移除元素前缀
     */
    public void remove(String queueName, String itemPrefix) {
        try {
            List<String> range = redisTemplate.opsForList().range(queueName, 0, -1);
            if (range == null) return;
            for (String item : range) {
                if (item.startsWith(itemPrefix)) {
                    redisTemplate.opsForList().remove(queueName, 0, item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取队列的大小
     * @param queueName 队列名
     * @return 结果
     */
    public int size(String queueName) {
        try {
            Long size = redisTemplate.opsForList().size(queueName);
            return size == null ? 0 : size.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 清空队列
     * @param queueName 队列名
     * @return 结果
     */
    public void clear(String queueName) {
        try {
            List<String> range = redisTemplate.opsForList().range(queueName, 0, -1);
            if (range != null) {
                range.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除队列
     * @param queueNames 队列名
     * @return 结果
     */
    public void delete(String... queueNames) {
        if (queueNames != null && queueNames.length > 0) {
            if (queueNames.length == 1) {
                redisTemplate.delete(queueNames[0]);
            } else {
                redisTemplate.delete(Arrays.asList(queueNames));
            }
        }
    }

    /**
     * 给队列中某个元素设置过期时间, 默认当元素存在过期时间后，那么就不设置过期时间
     * @param queueName  队列名
     * @param index      元素索引
     * @param expireTime 过期时间（毫秒）
     * @return 结果
     */
    public boolean setExpire(String queueName, int index, long expireTime) {
        try {
            long expire = this.getExpire(queueName, index);
            if (expire > 0L) {
                return true;
            }
            if (expireTime > 0) {
                redisTemplate.expire( queueName + "[" + index + "]", expireTime, TimeUnit.MILLISECONDS);
            } else {
                redisTemplate.expire( queueName + "[" + index + "]", 0L, TimeUnit.MILLISECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取队列中某个元素的过期时间
     * @param queueName 队列名
     * @param index     元素索引
     * @return 结果
     */
    private long getExpire(String queueName, int index) {
        try {
            Long expire = redisTemplate.getExpire(queueName + "[" + index + "]", TimeUnit.MILLISECONDS);
            return expire == null ? 0L : expire;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
