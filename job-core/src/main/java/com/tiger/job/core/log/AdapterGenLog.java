package com.tiger.job.core.log;

import com.tiger.job.core.log.impl.GenFailLog;
import com.tiger.job.core.log.impl.GenSuccessLog;
import org.springframework.stereotype.Component;

/**
 * @ClassName AdapterGenLog
 * @Description TODO
 * @Author huxuehao
 **/
@Component
public class AdapterGenLog {
    private final GenFailLog genFailLog;
    private final GenSuccessLog genSuccessLog;

    public AdapterGenLog(GenFailLog genFailLog, GenSuccessLog genSuccessLog) {
        this.genFailLog = genFailLog;
        this.genSuccessLog = genSuccessLog;
    }

    public GenLog matchGenLog(String message) {
        return message == null? genSuccessLog : genFailLog;
    }
}
