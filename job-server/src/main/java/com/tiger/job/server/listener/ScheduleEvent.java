package com.tiger.job.server.listener;

import com.tiger.job.common.entity.ScheduleTaskDto;
import org.springframework.context.ApplicationEvent;

/**
 * 描述：调度事件
 *
 * @author huxuehao
 **/
public class ScheduleEvent extends ApplicationEvent {
    private static final long serialVersionUID = -1L;
    private String type;

    private ScheduleTaskDto taskDto;

    public ScheduleEvent(Object source, String type, ScheduleTaskDto taskDto) {
        super(source);
        this.type = type;
        this.taskDto = taskDto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ScheduleTaskDto getTaskDto() {
        return taskDto;
    }

    public void setTaskDto(ScheduleTaskDto taskDto) {
        this.taskDto = taskDto;
    }
}
