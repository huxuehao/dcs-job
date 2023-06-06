package com.tiger.job.task.scheduled;

import com.tiger.job.common.annotation.TaskPath;
import com.tiger.job.common.annotation.SchedulerBean;

/**
 * @ClassName 测试定时任务
 * @Description TODO
 * @Author huxuehao
 **/
@SchedulerBean
public class MyScheduled1 {

    @TaskPath(path = "/sheduled1/execute/do-task1")
    public void doTask1() {
        System.out.println("The sheduled1-doTask1 task is executing now....");
    }

    @TaskPath(path = "/sheduled1/execute/do-task2")
    public void doTask2() {
        System.out.println("The sheduled1-doTask2 task is executing now....");
    }

    @TaskPath(path = "/sheduled1/execute/do-task3")
    public void doTask3() {
        System.out.println("The sheduled1-doTask3 task is executing now....");
    }

    @TaskPath(path = "/sheduled1/execute/do-task4")
    public void doTask4() {
        System.out.println("The sheduled1-doTask4 task is executing now....");
    }
}
