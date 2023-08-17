package com.tiger.job.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.common.constant.ChannelConstant;
import com.tiger.job.common.constant.ClusterProperties;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.entity.ScheduleTaskPo;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.server.listener.ScheduleEvent;
import com.tiger.job.server.mapper.ScheduleTaskMapper;
import com.tiger.job.server.service.ScheduleTaskService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

/**
 * @ClassName ScheduleTaskServiceImpl
 * @Description TODO
 * @Author huxuehao
 **/
@Service
public class ScheduleTaskServiceImpl extends ServiceImpl<ScheduleTaskMapper, ScheduleTaskDto> implements ScheduleTaskService {
    private final ApplicationEventPublisher publisher;
    private final ScheduleTaskMapper scheduleTaskMapper;
    //private final StringRedisTemplate stringRedisTemplate;
    private final ClusterProperties clusterProperties;
    private final Map<String, Consumer<ScheduleTaskDto>> triggerMap;

    public ScheduleTaskServiceImpl(ApplicationEventPublisher publisher, @Qualifier("triggerMap") Map<String, Consumer<ScheduleTaskDto>> triggerMap, ClusterProperties clusterProperties, ScheduleTaskMapper scheduleTaskMapper) {
        this.publisher = publisher;
        this.triggerMap = triggerMap;
        this.clusterProperties = clusterProperties;
        this.scheduleTaskMapper = scheduleTaskMapper;
    }


    @Override
    public List<ScheduleTaskDto> selectAll() {
        return scheduleTaskMapper.selectAll();
    }

    /* 新增*/
    public int add(ScheduleTaskDto scheduleTask){
        String id = MeUtil.nextId();
        String createUser = MeUtil.nextId();
        String createTime = MeUtil.currentDatetime();
        scheduleTask.setId(id);
        scheduleTask.setEnable("0");
        scheduleTask.setCreateUser(createUser);
        scheduleTask.setCreateTime(createTime);
        return scheduleTaskMapper.add(scheduleTask);
    }
    /* 更新*/
    @Transactional(rollbackFor = Exception.class)
    public int update(ScheduleTaskDto scheduleTask){
        String updateUser = MeUtil.nextId();
        String updateTime = MeUtil.currentDatetime();
        scheduleTask.setUpdateUser(updateUser);
        scheduleTask.setUpdateTime(updateTime);
        int res = scheduleTaskMapper.update(scheduleTask);
        if ("1".equals(scheduleTask.getEnable()) && res > 0) {
            this.openSchedule(Collections.singletonList(scheduleTask.getId()));

        }
        return res;
    }

    @Override
    public int getTotals(String taskName, String taskType, String taskStatus) {
        return scheduleTaskMapper.getTotals(taskName, taskType, taskStatus);
    }

    /* 分页 */
    public List<ScheduleTaskPo> getPage(Integer current, Integer size, String taskName, String taskType, String taskStatus) {
        return scheduleTaskMapper.getPage(current, size, taskName, taskType, taskStatus);
    }
    /* 启用 */
    @Transactional(rollbackFor = Exception.class)
    public int enableByIds(List<String> ids){
        int res = 0;
        if (ids != null && ids.size() > 0) {
            this.openSchedule(ids);
            res = scheduleTaskMapper.enableByIds(fillTaskInfo(ids));
        }
        return res;
    }
    /* 禁用 */
    @Transactional(rollbackFor = Exception.class)
    public int disableByIds(List<String> ids){
        int res = 0;
        if (ids != null && ids.size() > 0) {
            this.closeSchedule(ids);
            res = scheduleTaskMapper.disableByIds(fillTaskInfo(ids));
        }
        return res;
    }

    /* 删除 */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<String> ids) {
        int res = 0;
        if (ids != null && ids.size() > 0) {
            this.deleteSchedule(ids);
            res = scheduleTaskMapper.deleteByIds(ids);
        }
        return res;
    }

    @Override
    public ScheduleTaskPo refreshResult(String taskId) {
        return scheduleTaskMapper.refreshResult(taskId);
    }

    /* 填充id、更新人、更新时间 */
    private List<ScheduleTaskDto> fillTaskInfo (List<String> ids) {
        String updateUser = MeUtil.nextId();
        String updateTime = MeUtil.currentDatetime();
        List<ScheduleTaskDto> tasks = new ArrayList<>();
        ids.forEach(item -> {
            ScheduleTaskDto task = new ScheduleTaskDto();
            task.setId(item);
            task.setUpdateUser(updateUser);
            task.setUpdateTime(updateTime);
            tasks.add(task);
        });
        return tasks;
    }

    /* 开启定时 */
    private void openSchedule(List<String> ids) {
        List<ScheduleTaskDto> taskList = this.listByIds(ids);
        if (taskList != null && taskList.size() > 0) {
            taskList.forEach(item -> {
                if (clusterProperties.isOpen()) {
                    /* 发布消息,通知本节点及其他节点开启定时 */
                    //stringRedisTemplate.convertAndSend(ChannelConstant.OPEN, JSON.toJSONString(item));
                    publisher.publishEvent(new ScheduleEvent(this, ChannelConstant.OPEN, item));
                } else {
                    triggerMap.get(ChannelConstant.OPEN).accept(item);
                }
            });
        }
    }
    /* 关闭定时 */
    private void closeSchedule(List<String> ids) {
        List<ScheduleTaskDto> taskList = this.listByIds(ids);
        if (taskList != null && taskList.size() > 0) {
            taskList.forEach(item -> {
                if (clusterProperties.isOpen()) {
                    /* 发布消息，通知本节点及其他节点关闭定时 */
                    //stringRedisTemplate.convertAndSend(ChannelConstant.CLOSE, JSON.toJSONString(item));
                    publisher.publishEvent(new ScheduleEvent(this, ChannelConstant.CLOSE, item));
                } else {
                    triggerMap.get(ChannelConstant.CLOSE).accept(item);
                }
            });
        }
    }
    /* 删除定时 */
    private void deleteSchedule(List<String> ids) {
        List<ScheduleTaskDto> taskList = this.listByIds(ids);
        if (taskList != null && taskList.size() > 0) {
            taskList.forEach(item -> {
                if (clusterProperties.isOpen()) {
                    /* 发送消息，通知本节点及其他节点删除定时 */
                    //stringRedisTemplate.convertAndSend(ChannelConstant.DELETE, JSON.toJSONString(item));
                    publisher.publishEvent(new ScheduleEvent(this, ChannelConstant.CLOSE, item));
                } else {
                    triggerMap.get(ChannelConstant.DELETE).accept(item);
                }
            });
        }
    }

}
