package com.tiger.job.server.service.impl;

import com.tiger.job.common.constant.LogProperties;
import com.tiger.job.server.mapper.LogRotateMapper;
import com.tiger.job.server.service.LogRotate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @ClassName LogRotateImpl
 * @Description TODO
 * @Author huxuehao
 **/
@Service
public class LogRotateImpl implements LogRotate {

    private final String TABLE_NAME = "sys_scheduled_log";
    private final LogRotateMapper logRotateMapper;
    private final LogProperties logProperties;

    public LogRotateImpl(LogRotateMapper logRotateMapper, LogProperties logProperties) {
        this.logRotateMapper = logRotateMapper;
        this.logProperties = logProperties;
    }

    @Override
    public Boolean validDayCycle() throws ParseException {
        String dayCycleStr = logRotateMapper.selectDayCycle();
        if (dayCycleStr == null || "".equals(dayCycleStr)) {
            return false;
        }
        String[] split = dayCycleStr.split(",");
        if (split[0].equals(split[1])) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long minTime = dateFormat.parse(split[0]).getTime();
        long maxTime = dateFormat.parse(split[1]).getTime();

        return maxTime - minTime >= 1000 * 60 * 60 * 24 * logProperties.getSaveDays() ? true : false;
    }

    @Override
    public Boolean doRotate(String tableSuffix) throws ParseException {
        if (this.validDayCycle()) {
            logRotateMapper.copyTable(TABLE_NAME + tableSuffix);
            logRotateMapper.delRecordsIFExist(TABLE_NAME + tableSuffix);
            return true;
        }
        return false;
    }
}
