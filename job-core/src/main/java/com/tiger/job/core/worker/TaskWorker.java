package com.tiger.job.core.worker;

import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.constant.ClusterProperties;
import com.tiger.job.core.executor.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @InterfaceName Worker
 * @Description 开启定时任务需要传入Runnable接口的实现类，作为scheduledFuture.schedule(worker, cronTrigger)中的第一个参数，
 * 所以"工作内容"实现Runnable接口是必须要做的。在run()中要执行的内容，我们可以封装成任务执行器TaskExecutor。
 *
 * 注意：对于任务执行器（TaskExecutor）我们最好不使用（也要看实际情况）spring的bean进行注入，因为使用bean后，上下文中始终使用的是一个
 * 对象，可能会给你带来额外的问题（反正我是遇到了），建议使用构造函数传参。
 * @Author huxuehao
 **/
@Component
public class TaskWorker implements Runnable {
    private TaskExecutor taskExecutor;
    private ScheduleTaskDto scheduleTask;
    private ClusterProperties clusterProperties;

    public TaskWorker() {
    }

    public TaskWorker(ScheduleTaskDto scheduleTask, TaskExecutor taskExecutor, ClusterProperties clusterProperties) {
        this.scheduleTask = scheduleTask;
        this.taskExecutor = taskExecutor;
        this.clusterProperties = clusterProperties;
    }

    public void run() {
        if (clusterProperties.isOpen()) {
            taskExecutor.clusterExecute(scheduleTask);
        } else {
            taskExecutor.execute(scheduleTask);
        }
    }
}
