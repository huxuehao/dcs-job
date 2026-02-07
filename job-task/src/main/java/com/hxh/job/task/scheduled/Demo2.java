package com.hxh.job.task.scheduled;

import com.hxh.job.common.JobHandler;
import com.hxh.job.task.topic.ComponentBean;
import org.springframework.beans.factory.annotation.Autowired;

public class Demo2 extends JobHandler {

    @Autowired
    private ComponentBean componentBean;
    @Override
    public void execute() throws Exception {
        // do something
        System.out.println("do something...");
        componentBean.invoke();
        throw new RuntimeException("错误日志查看测试");
    }
}
