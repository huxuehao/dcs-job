package com.hxh.job.core.controller;

import com.hxh.job.common.annotation.MenuTag;
import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.common.r.R;
import com.hxh.job.core.executor.impl.ManualExecutor;
import com.hxh.job.server.service.ScheduleTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping(value = "/job/task")
@MenuTag(code = "job_task")
public class TaskExecutorController {
    private final ManualExecutor executor;
    private final ScheduleTaskService scheduleTaskService;

    public TaskExecutorController(ManualExecutor executor, ScheduleTaskService scheduleTaskService) {
        this.executor = executor;
        this.scheduleTaskService = scheduleTaskService;
    }

    @PreAuthorize("@ps.hasPermission('get::job:task:execute')")
    @GetMapping(value = "/execute", name = "执行任务")
    public R<?> execute(@RequestParam("id") String id) {
        ScheduledConfigEntity task = scheduleTaskService.getById(id);
        Boolean execute = executor.execute(task);
        if (execute == null) {
            return R.fail("操作冲突：定时正在执行，请稍后重试");
        }
        return execute? R.success("执行成功") : R.success("[ "+task.getName()+" ] 执行失败");
    }
}
