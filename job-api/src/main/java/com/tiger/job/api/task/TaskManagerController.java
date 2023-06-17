package com.tiger.job.api.task;

import com.tiger.job.common.annotation.LoginAuth;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.entity.ScheduleTaskPo;
import com.tiger.job.common.response.Response;
import com.tiger.job.core.executor.impl.ManualExecutor;
import com.tiger.job.server.service.ScheduleLogService;
import com.tiger.job.server.service.ScheduleTaskService;
import org.springframework.context.annotation.Description;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TaskManagerController
 * @Description 任务管理接口，这里我就使用构造函数进行依赖注入了
 * @Author huxuehao
 **/
@RestController
@RequestMapping(value = "/job/task")
public class TaskManagerController {
    private final ScheduleTaskService scheduleTaskService;
    private final ScheduleLogService scheduleLogService;
    private final ManualExecutor executor;

    public TaskManagerController(ScheduleTaskService scheduleTaskService, ScheduleLogService scheduleLogService, ManualExecutor executor) {
        this.scheduleTaskService = scheduleTaskService;
        this.scheduleLogService = scheduleLogService;
        this.executor = executor;
    }

    @LoginAuth
    @Description(value = "获取最新的错误日志")
    @GetMapping(value = "/log/by-task-id")
    public Response latestLogByTask(@RequestParam("taskId") String taskId) {
        return Response.success(scheduleLogService.latestLogByTask(taskId));
    }

    @LoginAuth
    @Description(value = "上一条日志")
    @GetMapping(value = "/log/last")
    public Response lastLog(@RequestParam("taskId") String taskId,
                     @RequestParam("executeTime") String executeTime) {
        return Response.success(scheduleLogService.lastLog(taskId, executeTime));
    }

    @LoginAuth
    @Description(value = "下一条日志")
    @GetMapping(value = "/log/next")
    public Response nextLog(@RequestParam("taskId") String taskId,
                     @RequestParam("executeTime") String executeTime) {
        return Response.success(scheduleLogService.nextLog(taskId, executeTime));
    }

    @LoginAuth
    @Description(value = "执行任务")
    @GetMapping(value = "/execute")
    public Response execute(@RequestParam("id") String id) {
        ScheduleTaskDto task = scheduleTaskService.getById(id);
        Boolean execute = executor.execute(task);
        if (execute == null) {
            return Response.error("操作冲突：定时正在执行，请稍后重试");
        }
        return execute? Response.success("执行成功") : Response.success("[ "+task.getName()+" ] 执行失败");
    }

    @LoginAuth
    @Description(value = "刷新执行结果")
    @GetMapping(value = "/refresh-result")
    public Response refreshResult(@RequestParam("id") String id) {
        return Response.success(scheduleTaskService.refreshResult(id));
    }

    @LoginAuth
    @Description(value = "添加任务")
    @PostMapping(value = "/add")
    public Response addTask(@RequestBody ScheduleTaskDto scheduleTask) {
        if (!isValidCronExpression(scheduleTask.getCron())) {
            return Response.error("cron表达式校验失败");
        }
        scheduleTaskService.add(scheduleTask);
        return Response.success("操作成功");
    }

    @LoginAuth
    @Description(value = "更新任务")
    @PostMapping(value = "/update")
    public Response updateTask(@RequestBody ScheduleTaskDto scheduleTask) {
        if (!isValidCronExpression(scheduleTask.getCron())) {
            return Response.error("cron表达式校验失败");
        }
        return Response.success(scheduleTaskService.update(scheduleTask));
    }

    @LoginAuth
    @Description(value = "删除任务")
    @PostMapping(value = "/delete")
    public Response deleteTask(@RequestBody List<String> ids) {
        return Response.success(scheduleTaskService.deleteByIds(ids));
    }

    @LoginAuth
    @Description(value = "暂停任务")
    @PostMapping(value = "/disable")
    public Response disableTask(@RequestBody List<String> ids) {
        return Response.success(scheduleTaskService.disableByIds(ids));
    }

    @LoginAuth
    @Description(value = "启动任务")
    @PostMapping(value = "/enable")
    public Response enableTask(@RequestBody List<String> ids) {
        return Response.success(scheduleTaskService.enableByIds(ids));
    }

    @LoginAuth
    @Description(value = "获取任务详情")
    @GetMapping(value = "/detail")
    public Response taskDetail(@RequestParam("id") String id) {
        return Response.success(scheduleTaskService.getById(id));
    }

    @LoginAuth
    @Description(value = "获取任务分页")
    @GetMapping(value = "/page")
    public Response taskPage(@RequestParam(required = false, value = "current") Integer current,
                      @RequestParam(required = false, value = "size") Integer size,
                      @RequestParam(required = false, value = "taskName") String taskName,
                      @RequestParam(required = false, value = "taskType") String taskType,
                      @RequestParam(required = false, value = "taskStatus") String taskStatus) {
        int total = scheduleTaskService.getTotals(taskName, taskType, taskStatus);
        List<ScheduleTaskPo> page = scheduleTaskService.getPage((current - 1) * size, size, taskName, taskType, taskStatus);
        Map<String, Object> pageRes = new LinkedHashMap<>();
        int pages = total / size + (total % size == 0 ? 0: 1);
        pageRes.put("pages",pages);
        pageRes.put("records",page);
        pageRes.put("size",size);
        pageRes.put("current",current);
        pageRes.put("total",total);
        return Response.success(pageRes);
    }

    @LoginAuth
    @Description(value = "获取任务列表")
    @GetMapping(value = "/list")
    public Response taskList() {
        List<ScheduleTaskDto> list = scheduleTaskService.list();
        return Response.success(list);
    }

    /* 检验cron表达式是否有效 */
    private boolean isValidCronExpression(String cronStr) {
        return CronExpression.isValidExpression(cronStr);
    }
}
