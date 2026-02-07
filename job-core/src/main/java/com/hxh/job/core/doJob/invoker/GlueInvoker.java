package com.hxh.job.core.doJob.invoker;

import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.common.util.Func;
import com.hxh.job.common.util.MeUtil;
import com.hxh.job.core.doJob.AbstractInvoker;
import com.hxh.job.core.doJob.glue.GlueFactory;
import com.hxh.job.common.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 描述：基于Glue模式的执行器
 *
 * @author huxuehao
 **/
@Component
public class GlueInvoker extends AbstractInvoker {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String invoke(ScheduledConfigEntity task) {
        if (Func.isEmpty(task.getConfig())) {
            log.error("定时任务[{}]执行失败", task.getName());
            return "Glue 脚本为空";
        }

        String message = null;
        try {
            JobHandler jobHandler = GlueFactory.getFactory().loadNewInstance(task.getConfig());
            jobHandler.init();
            try {
                jobHandler.execute();
            } finally {
                jobHandler.destroy();
            }
        } catch (Exception e) {
            log.error("定时任务[{}]执行失败", task.getName());
            message = MeUtil.catchExceptionStackInfo(e);
        }
        return message;
    }
}
