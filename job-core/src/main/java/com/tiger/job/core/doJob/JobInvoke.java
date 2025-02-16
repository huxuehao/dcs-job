package com.tiger.job.core.doJob;

import com.alibaba.fastjson2.JSON;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.enums.JobType;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.core.doJob.invoker.AnnotationInvoker;
import com.tiger.job.core.doJob.invoker.GlueInvoker;
import com.tiger.job.core.doJob.invoker.TemplateInvoker;
import com.tiger.job.core.log.TaskLog;
import org.springframework.stereotype.Component;

/**
 * 描述：定时任务执行
 *
 * @author huxuehao
 **/
@Component
public class JobInvoke {
    private final AnnotationInvoker annotationInvoker;
    private final TemplateInvoker templateInvoker;
    private final GlueInvoker glueInvoker;
    private final TaskLog taskLog;

    public JobInvoke(AnnotationInvoker annotationInvoker, TemplateInvoker templateInvoker, GlueInvoker glueInvoker, TaskLog taskLog) {
        this.annotationInvoker = annotationInvoker;
        this.templateInvoker = templateInvoker;
        this.glueInvoker = glueInvoker;
        this.taskLog = taskLog;
    }

    public boolean execute(ScheduledConfigEntity task) {
        String message = null;
        try {
            switch (JobType.valueOf(task.getType())) {
                case ANNOTATION :
                    message = annotationInvoker.invoke(task);
                    break;
                case TEMPLATE :
                    message = templateInvoker.invoke(task);
                    break;
                case GLUE :
                    message = glueInvoker.invoke(task);
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
