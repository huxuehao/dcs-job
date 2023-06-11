package com.tiger.job.common.constant;

/**
 * @ClassName ChannelConstant
 * @Description redis的订阅频道的channel名称
 * @Author huxuehao
 **/
public class ChannelConstant {
    public static final String OPEN = "tiger-job-channel:open-task";
    public static final String CLOSE = "tiger-job-channel:close-task";
    public static final String DELETE = "tiger-job-channel:delete-task";
}
