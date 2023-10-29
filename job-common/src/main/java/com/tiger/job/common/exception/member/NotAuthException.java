package com.tiger.job.common.exception.member;

import com.tiger.job.common.enums.ResponseStatus;
import com.tiger.job.common.exception.base.BaseException;

/**
 * 描述：请求未授权异常
 *
 * @author huxuehao
 **/
public class NotAuthException extends BaseException {
    public NotAuthException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public NotAuthException(String module, String method, String message) {
        super(ResponseStatus.NotAuth.code, module, method, message);
    }

    public NotAuthException(String message) {
        super(ResponseStatus.NotAuth.code, null, null, message);
    }

    public NotAuthException() {
        super(ResponseStatus.NotAuth.code, null, null, ResponseStatus.NotAuth.msg);
    }
}
