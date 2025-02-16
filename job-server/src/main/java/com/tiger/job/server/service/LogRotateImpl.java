package com.tiger.job.server.service;

import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.constant.LogProperties;
import com.tiger.job.server.mapper.LogRotateMapper;
import com.tiger.job.server.service.LogRotate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日志轮转实现类
 *
 * @author huxuehao
 **/
@Service
public class LogRotateImpl implements LogRotate {

    private final LogRotateMapper logRotateMapper;
    private final LogProperties logProperties;

    public LogRotateImpl(LogRotateMapper logRotateMapper, LogProperties logProperties) {
        this.logRotateMapper = logRotateMapper;
        this.logProperties = logProperties;
    }

    @Override
    public Boolean validDayCycle() throws ParseException {
        String dayCycleStr = logRotateMapper.selectDayCycle(DBConst.SYS_SCHEDULED_LOG);
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

        return maxTime - minTime >= 1000L * 60 * 60 * 24 * logProperties.getSaveDays();
    }

    @Override
    public void doRotate(String tableSuffix) throws ParseException {
        if (this.validDayCycle()) {
            logRotateMapper.copyTable(DBConst.SYS_SCHEDULED_LOG + tableSuffix);
            logRotateMapper.delRecordsIFExist(DBConst.SYS_SCHEDULED_LOG + tableSuffix);
        }
    }
}
