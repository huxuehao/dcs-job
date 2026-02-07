package com.hxh.job.common.exception.member;

import com.hxh.job.common.enums.ResponseStatus;
import com.hxh.job.common.exception.base.BaseException;

/**
 * 描述：请求超时异常
 *
 * @author huxuehao
 **/
public class TimeoutException extends BaseException {
    public TimeoutException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public TimeoutException(String module, String method, String message) {
        super(ResponseStatus.Timeout.code, module, method, message);
    }

    public TimeoutException(String message) {
        super(ResponseStatus.Timeout.code, null, null, message);
    }

    public TimeoutException() {
        super(ResponseStatus.Timeout.code, null, null, ResponseStatus.Timeout.msg);
    }
}
