package com.tiger.job.core.doJob;

import com.alibaba.fastjson2.JSON;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.enums.JobType;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.core.log.TaskLog;
import org.springframework.stereotype.Component;

/**
 * 描述：定时任务执行
 *
 * @author huxuehao
 **/
@Component
public class JobInvoke {
    private final InvokeAnnotation invokeAnnotation;
    private final InvokeTemplate invokeTemplate;
    private final TaskLog taskLog;

    public JobInvoke(InvokeAnnotation invokeAnnotation, InvokeTemplate invokeTemplate, TaskLog taskLog) {
        this.invokeAnnotation = invokeAnnotation;
        this.invokeTemplate = invokeTemplate;
        this.taskLog = taskLog;
    }

    public boolean execute(ScheduledConfigEntity task) {
        String message = null;
        try {
            switch (JobType.valueOf(task.getType())) {
                case ANNOTATION :
                    message = invokeAnnotation.invoke(task);
                    break;
                case TEMPLATE :
                    message = invokeTemplate.invoke(task);
                    break;
                default:
                    message = "匹配" + JSON.toJSONString(task) + "中的type失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = MeUtil.catchExceptionStackInfo(e);
        } finally {
            taskLog.invoke(task, message);
        }
        return message == null;
    }
}
