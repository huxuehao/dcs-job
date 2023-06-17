package com.tiger.job.core.doJob;

import com.tiger.job.common.constant.LogProperties;
import com.tiger.job.common.entity.ScheduleLogDto;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.exception.member.NotMatchPathException;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.server.service.ScheduleLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ClassName Job
 * @Description 定时任务执行
 * @Author huxuehao
 **/
@Component
public class Job {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<String, Map<Object, Method>> schedulerScanMethodMap;
    private final LogProperties logProperties;
    private final ScheduleLogService scheduleLogService;

    public Job(@Qualifier("schedulerScanMethodMap") Map<String, Map<Object, Method>> schedulerScanMethodMap, LogProperties logProperties, ScheduleLogService scheduleLogService) {
        this.schedulerScanMethodMap = schedulerScanMethodMap;
        this.logProperties = logProperties;
        this.scheduleLogService = scheduleLogService;
    }


    public boolean invoke(ScheduleTaskDto task) {
        String message = null;
        try {
            /*执行定时任务*/
            if (schedulerScanMethodMap.containsKey(task.getPath())) {
                Map<Object, Method> objectMethodMap = schedulerScanMethodMap.get(task.getPath());
                /* objectMethodMap 中的Key是已经实例化过的对象，value是定时任务的方法载体 */
                for (Map.Entry<Object, Method> entry : objectMethodMap.entrySet()) {
                    /* 执行定时任务*/
                    entry.getValue().invoke(entry.getKey());
                    break;
                }
            } else {
                throw new NotMatchPathException("path[" + task.getPath() + "]未匹配到对应的定时任务");
            }
            log.info("定时任务[{}]执行成功", task.getName());
        } catch (Exception e) {
            log.error("定时任务[{}]执行失败", task.getName());
            /* 将异常栈信息存入字符串 */
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            message = sw.toString();
        } finally {
            /* 生成日志 */
            this.genLog(task, message);
        }
        return message == null;
    }

    /* 定时任务日志采集 */
    private void genLog(ScheduleTaskDto task, String message){
        /* 判断定时任务执行状态。true为执行成功，false为执行失败 */
        String status = message == null ? "success" : "fail";
        /* 错误日志和成功日志皆未开启时 */
        if (!logProperties.isFailOpen() && !logProperties.isSuccessOpen()) {
            return;
        }
        /* 成功日志未开启时 */
        if ("success".equals(status) && !logProperties.isSuccessOpen()) {
            return;
        }
        /* 失败日志未开启时 */
        if ("fail".equals(status) && !logProperties.isFailOpen()) {
            return;
        }
        /* 日志未开启时 */
        if ("0".equals(task.getOpenLog())) {
            return;
        }
        try {
            /* 初始化日志信息 */
            ScheduleLogDto scheduleLog = new ScheduleLogDto(
                    MeUtil.nextId(),
                    task.getId(),
                    task.getName(),
                    status,
                    message,
                    MeUtil.currentDatetime()
            );
            /* 生成日志 */
            scheduleLogService.add(scheduleLog);
        } catch (Exception e) {
            log.error("[error]日志持久化错误");
        }
    }
}
