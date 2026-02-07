package com.hxh.job.common.exception.member;

import com.hxh.job.common.enums.ResponseStatus;
import com.hxh.job.common.exception.base.BaseException;

/**
 * 描述：错误
 *
 * @author huxuehao
 **/
public class ErrorException extends BaseException {
    public ErrorException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public ErrorException(String module, String method, String message) {
        super(ResponseStatus.Error.code, module, method, message);
    }

    public ErrorException(String message) {
        super(ResponseStatus.Error.code, null, null, message);
    }

    public ErrorException() {
        super(ResponseStatus.Error.code, null, null, ResponseStatus.Error.msg);
    }
}
