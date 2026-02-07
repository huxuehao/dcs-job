package com.hxh.job.server.listener;

import com.hxh.job.common.entity.ScheduledConfigEntity;
import org.springframework.context.ApplicationEvent;

/**
 * 描述：调度事件
 *
 * @author huxuehao
 **/
public class ScheduleEvent extends ApplicationEvent {
    private static final long serialVersionUID = -1L;
    private String type;

    private ScheduledConfigEntity taskDto;

    public ScheduleEvent(Object source, String type, ScheduledConfigEntity taskDto) {
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

    public ScheduledConfigEntity getTaskDto() {
        return taskDto;
    }

    public void setTaskDto(ScheduledConfigEntity taskDto) {
        this.taskDto = taskDto;
    }
}
