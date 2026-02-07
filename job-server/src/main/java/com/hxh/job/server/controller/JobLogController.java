package com.hxh.job.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hxh.job.common.annotation.MenuTag;
import com.hxh.job.common.entity.ScheduleLogEntity;
import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.common.mp.support.MP;
import com.hxh.job.common.mp.support.PageParams;
import com.hxh.job.common.r.R;
import com.hxh.job.common.util.Func;
import com.hxh.job.server.service.ScheduleLogService;
import com.hxh.job.server.service.ScheduleTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：任务管理接口，这里我就使用构造函数进行依赖注入了
 *
 * @author huxuehao
 */
@RestController
@RequestMapping(value = "/job/log")
@MenuTag(code = "job_log")
public class JobLogController {
    private final ScheduleLogService scheduleLogService;
    private final ScheduleTaskService scheduleTaskService;


    public JobLogController(ScheduleLogService scheduleLogService, ScheduleTaskService scheduleTaskService) {
        this.scheduleLogService = scheduleLogService;
        this.scheduleTaskService = scheduleTaskService;
    }

    @PreAuthorize("@ps.hasPermission('get::job:log:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> taskPage(ScheduleLogEntity log, PageParams pageParams) {
        List<Long> taskIds = null;
        if (Func.isNotEmpty(log.getTaskName())) {
            QueryWrapper<ScheduledConfigEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", log.getTaskName());
            List<ScheduledConfigEntity> list = scheduleTaskService.list(queryWrapper);
            if (!list.isEmpty()) {
                taskIds = list.stream().map(ScheduledConfigEntity::getId).collect(Collectors.toList());
            }

        }
        log.setTaskName(null);

        QueryWrapper<ScheduleLogEntity> qw = MP.getQueryWrapper(log);
        if (taskIds != null) {
            qw.in("task_id", taskIds);
        }
        qw.orderByDesc("execute_time");
        qw.select("id","task_id","task_name","execute_status", "execute_time");

        return R.data(scheduleLogService.page(MP.getPage(pageParams), qw));
    }

    @PreAuthorize("@ps.hasPermission('get::job:log:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(scheduleLogService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('post::job:log:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<String> ids) {
        return R.data(scheduleLogService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::job:log:latest-by-task-id')")
    @GetMapping(value = "/latest-by-task-id", name = "获取最新的错误日志")
    public R<?> latestLogByTask(@RequestParam("taskId") String taskId) {
        return R.data(scheduleLogService.latestLogByTask(taskId));
    }

    @PreAuthorize("@ps.hasPermission('get::job:log:last')")
    @GetMapping(value = "/last", name = "上一条日志")
    public R<?> lastLog(@RequestParam("taskId") String taskId,
                     @RequestParam("executeTime") String executeTime) {
        return R.data(scheduleLogService.lastLog(taskId, executeTime));
    }

    @PreAuthorize("@ps.hasPermission('get::job:log:next')")
    @GetMapping(value = "/next", name = "下一条日志")
    public R<?> nextLog(@RequestParam("taskId") String taskId,
                     @RequestParam("executeTime") String executeTime) {
        return R.data(scheduleLogService.nextLog(taskId, executeTime));
    }
}
