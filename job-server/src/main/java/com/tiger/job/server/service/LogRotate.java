package com.tiger.job.server.service;

import java.text.ParseException;

/**
 * 日志轮转接口
 *
 * @author huxuehao
 **/
public interface LogRotate {

    /* 获取日志周期,以天为单位 */
    Boolean validDayCycle() throws ParseException;

    /* 执行日志轮转 */
    void doRotate(String tableSuffix) throws ParseException;
}
