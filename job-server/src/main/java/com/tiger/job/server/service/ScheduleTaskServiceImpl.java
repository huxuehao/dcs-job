package com.tiger.job.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.common.constant.ChannelConstant;
import com.tiger.job.common.constant.ClusterProperties;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.entity.ScheduleTaskPo;
import com.tiger.job.common.enums.JobType;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.server.listener.ScheduleEvent;
import com.tiger.job.server.mapper.ScheduleTaskMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

/**
 * 调度任务实现类
 *
 * @author huxuehao
 **/
@Service
public class ScheduleTaskServiceImpl extends ServiceImpl<ScheduleTaskMapper, ScheduledConfigEntity> implements ScheduleTaskService {
    private final ApplicationEventPublisher publisher;
    private final ScheduleTaskMapper scheduleTaskMapper;
    private final ClusterProperties clusterProperties;
    private final Map<String, Consumer<ScheduledConfigEntity>> triggerMap;

    public ScheduleTaskServiceImpl(ApplicationEventPublisher publisher, @Qualifier("triggerMap") Map<String, Consumer<ScheduledConfigEntity>> triggerMap, ClusterProperties clusterProperties, ScheduleTaskMapper scheduleTaskMapper) {
        this.publisher = publisher;
        this.triggerMap = triggerMap;
        this.clusterProperties = clusterProperties;
        this.scheduleTaskMapper = scheduleTaskMapper;
    }


    @Override
    public List<ScheduledConfigEntity> selectAll() {
        return list();
    }

    /* 新增*/
    public boolean add(ScheduledConfigEntity scheduleTask){
        if (JobType.ANNOTATION.toString().equals(scheduleTask.getType())) {
            QueryWrapper<ScheduledConfigEntity> qw = new QueryWrapper<>();
            qw.eq("type", JobType.ANNOTATION.toString());
            qw.eq("path", scheduleTask.getPath());
            List<ScheduledConfigEntity> list = list(qw);
            if (!list.isEmpty()) {
                throw new RuntimeException("执行路径已存在");
            }
        }

        String createUser = MeUtil.nextId();
        String createTime = MeUtil.currentDatetime();
        scheduleTask.setEnable(0);
        scheduleTask.setCreateUser(createUser);
        scheduleTask.setCreateTime(createTime);
        return save(scheduleTask);
    }

    /* 更新*/
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ScheduledConfigEntity scheduleTask){
        if (JobType.ANNOTATION.toString().equals(scheduleTask.getType())) {
            QueryWrapper<ScheduledConfigEntity> qw = new QueryWrapper<>();
            qw.eq("type", JobType.ANNOTATION.toString());
            qw.eq("path", scheduleTask.getPath());
            qw.ne("id", scheduleTask.getId());
            List<ScheduledConfigEntity> list = list(qw);
            if (!list.isEmpty()) {
                throw new RuntimeException("执行路径已存在");
            }
        }

        scheduleTask.setUpdateUser(MeUtil.nextId());
        scheduleTask.setUpdateTime(MeUtil.currentDatetime());
        boolean res = updateById(scheduleTask);

        if (scheduleTask.getEnable() == 1 && res) {
            this.openSchedule(Collections.singletonList(scheduleTask.getId()));
        } else if (scheduleTask.getEnable() == 0) {
            this.closeSchedule(Collections.singletonList(scheduleTask.getId()));
        }

        return res;
    }

    /* 启用 */
    @Transactional(rollbackFor = Exception.class)
    public int enableByIds(List<Long> ids){
        int res = 0;
        if (ids != null && ids.size() > 0) {
            this.openSchedule(ids);
            res = scheduleTaskMapper.enableByIds(DBConst.SYS_SCHEDULED, fillTaskInfo(ids));
        }
        return res;
    }

    /* 禁用 */
    @Transactional(rollbackFor = Exception.class)
    public int disableByIds(List<Long> ids){
        int res = 0;
        if (ids != null && ids.size() > 0) {
            this.closeSchedule(ids);
            res = scheduleTaskMapper.disableByIds(DBConst.SYS_SCHEDULED, fillTaskInfo(ids));
        }
        return res;
    }

    /* 删除 */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> ids) {
        boolean res = false;
        if (ids != null && ids.size() > 0) {
            this.deleteSchedule(ids);
            res = removeBatchByIds(ids);
        }
        return res;
    }

    @Override
    public ScheduleTaskPo refreshResult(Long taskId) {
        return scheduleTaskMapper.refreshResult(DBConst.SYS_SCHEDULED, DBConst.SYS_CLASSIFY, taskId);
    }

    /* 开启定时 */
    @Override
    public void openSchedule(List<Long> ids) {
        List<ScheduledConfigEntity> taskList = this.listByIds(ids);
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
    @Override
    public void closeSchedule(List<Long> ids) {
        List<ScheduledConfigEntity> taskList = this.listByIds(ids);
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

    /* 填充id、更新人、更新时间 */
    private List<ScheduledConfigEntity> fillTaskInfo (List<Long> ids) {
        String updateUser = MeUtil.nextId();
        String updateTime = MeUtil.currentDatetime();
        List<ScheduledConfigEntity> tasks = new ArrayList<>();
        ids.forEach(item -> {
            ScheduledConfigEntity task = new ScheduledConfigEntity();
            task.setId(item);
            task.setUpdateUser(updateUser);
            task.setUpdateTime(updateTime);
            tasks.add(task);
        });
        return tasks;
    }

    /* 删除定时 */
    private void deleteSchedule(List<Long> ids) {
        List<ScheduledConfigEntity> taskList = this.listByIds(ids);
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
