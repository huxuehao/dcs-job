package com.tiger.job.task.topic;

import org.springframework.stereotype.Component;

/**
 * spring的bean
 *
 * @author huxuehao
 **/
@Component
public class ComponentBean {

    public void invoke() {
        System.out.println("=======>do work invoke now...");
    }
}
