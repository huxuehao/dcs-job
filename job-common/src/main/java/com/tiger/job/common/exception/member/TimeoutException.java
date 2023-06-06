package com.tiger.job.common.exception.member;

import com.tiger.job.common.enums.ResponseStatus;
import com.tiger.job.common.exception.base.BaseException;

/**
 * @ClassName TimeoutException
 * @Description 请求超时异常
 * @Author StudiousTiger
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
