package com.tiger.job.task.scheduled;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;

/**
 * 测试定时任务
 *
 * @author huxuehao
 **/
@SchedulerBean
public class MyScheduled2 {
    @TaskPath(name = "定时任务5", path = "/sheduled2/execute/do-task1")
    public void doTask1() {
        System.out.println("The sheduled2-doTask1 task is executing now....");
    }

    @TaskPath(name = "定时任务6", path = "/sheduled2/execute/do-task2")
    public void doTask2() {
        System.out.println("The sheduled2-doTask2 task is executing now....");
    }

    @TaskPath(name = "定时任务7", path = "/sheduled2/execute/do-task3")
    public void doTask3() {
        System.out.println("The sheduled2-doTask3 task is executing now....");
    }

    @TaskPath(name = "定时任务8", path = "/sheduled2/execute/do-task4")
    public void doTask4() {
        System.out.println("The sheduled2-doTask4 task is executing now....");
    }
}
