package com.tiger.job.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tiger.job.common.annotation.MenuTag;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.mp.support.MP;
import com.tiger.job.common.mp.support.PageParams;
import com.tiger.job.common.r.R;
import com.tiger.job.server.service.ScheduleTaskService;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：任务管理接口，这里我就使用构造函数进行依赖注入了
 *
 * @author huxuehao
 */
@RestController
@RequestMapping(value = "/job/task")
@MenuTag(code = "job_task")
public class JobTaskController {
    private final ScheduleTaskService scheduleTaskService;

    public JobTaskController(ScheduleTaskService scheduleTaskService) {
        this.scheduleTaskService = scheduleTaskService;
    }

    @PreAuthorize("@ps.hasPermission('get::job:task:refresh-result')")
    @GetMapping(value = "/refresh-result", name = "刷新执行结果")
    public R<?> refreshResult(@RequestParam("id") Long id) {
        return R.data(scheduleTaskService.refreshResult(id));
    }

    @PreAuthorize("@ps.hasPermission('post::job:task:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> addTask(@RequestBody ScheduledConfigEntity scheduleTask) {
        // 检验cron表达式是否有效
        if (!CronExpression.isValidExpression(scheduleTask.getCron())) {
            return  R.fail("cron表达式校验失败");
        }
        scheduleTaskService.add(scheduleTask);
        return R.data("操作成功");
    }

    @PreAuthorize("@ps.hasPermission('post::job:task:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> updateTask(@RequestBody ScheduledConfigEntity scheduleTask) {
        // 检验cron表达式是否有效
        if (!CronExpression.isValidExpression(scheduleTask.getCron())) {
            return  R.fail("cron表达式校验失败");
        }
        return R.data(scheduleTaskService.update(scheduleTask));
    }

    @PreAuthorize("@ps.hasPermission('post::job:task:delete')")
    @PostMapping(value = "/delete", name = "删除任务")
    public R<?> deleteTask(@RequestBody List<Long> ids) {
        return R.data(scheduleTaskService.deleteByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('post::job:task:disable')")
    @PostMapping(value = "/disable", name = "暂停任务")
    public R<?> disableTask(@RequestBody List<Long> ids) {
        return R.data(scheduleTaskService.disableByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('post::job:task:enable')")
    @PostMapping(value = "/enable", name = "启动任务")
    public R<?> enableTask(@RequestBody List<Long> ids) {
        return R.data(scheduleTaskService.enableByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::job:task:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam("id") String id) {
        return R.data(scheduleTaskService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('get::job:task:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> taskPage(ScheduledConfigEntity config, PageParams pageParams) {
        QueryWrapper<ScheduledConfigEntity> qw = MP.getQueryWrapper(config);
        qw.select("id","name","task_classify","task_describe", "cron","path","enable","open_log","type");
        return R.data(scheduleTaskService.page(MP.getPage(pageParams),qw));
    }

    @PreAuthorize("@ps.hasPermission('get::job:task:list')")
    @GetMapping(value = "/list", name = "列表")
    public R<?> taskList() {
        QueryWrapper<ScheduledConfigEntity> qw = new QueryWrapper<>();
        qw.select("id","name","task_classify","task_describe", "cron","path","enable","open_log","type");
        List<ScheduledConfigEntity> list = scheduleTaskService.list(qw);
        return R.data(list);
    }


    @PreAuthorize("@ps.hasPermission('get::job:task:future-execution-plan')")
    @GetMapping(value = "/future-execution-plan", name = "未来执行计划")
    public R<?> futureExecutionPlan(@RequestParam String cron, @RequestParam int numTimes) {
        List<String> executionTimes = new ArrayList<>();

        // 解析 Cron 表达式
        CronExpression cronExpression = CronExpression.parse(cron);

        // 从当前时间开始计算
        LocalDateTime nextExecutionTime = LocalDateTime.now();
        // 格式化日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < numTimes; i++) {
            nextExecutionTime = cronExpression.next(nextExecutionTime);
            if (nextExecutionTime == null) {
                break; // 如果没有下一次执行时间，退出循环
            }
            // 将 LocalDateTime 转换为 Date
            Date date = Date.from(nextExecutionTime.atZone(ZoneId.systemDefault()).toInstant());
            executionTimes.add(formatter.format(date));
        }

        return R.data(executionTimes);
    }
}
