package com.hxh.job.common.exception.member;

import com.hxh.job.common.enums.ResponseStatus;
import com.hxh.job.common.exception.base.BaseException;

/**
 * 描述：未找到异常
 *
 * @author huxuehao
 **/
public class NotFoundException extends BaseException {
    public NotFoundException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public NotFoundException(String module, String method, String message) {
        super(ResponseStatus.NotFound.code, module, method, message);
    }

    public NotFoundException(String message) {
        super(ResponseStatus.NotFound.code, null, null, message);
    }

    public NotFoundException() {
        super(ResponseStatus.NotFound.code, null, null, ResponseStatus.NotFound.msg);
    }
}
