package com.tiger.job.task.scheduled;

import com.tiger.job.common.JobHandler;
import com.tiger.job.task.topic.ComponentBean;
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
