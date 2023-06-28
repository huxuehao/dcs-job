package com.tiger.job.core.doJob;

import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.exception.member.NotMatchPathException;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.core.log.TaskLog;
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
    private final TaskLog taskLog;

    public Job(@Qualifier("schedulerScanMethodMap") Map<String, Map<Object, Method>> schedulerScanMethodMap, TaskLog taskLog) {
        this.schedulerScanMethodMap = schedulerScanMethodMap;
        this.taskLog = taskLog;
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
            message = MeUtil.catchExceptionStackInfo(e);
        } finally {
            /* 生成日志 */
            taskLog.invoke(task, message);
        }
        return message == null;
    }
}
