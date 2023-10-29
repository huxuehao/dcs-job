package com.tiger.job.common.exception.member;

import com.tiger.job.common.enums.ResponseStatus;
import com.tiger.job.common.exception.base.BaseException;

/**
 * 描述：请求未授权异常
 *
 * @author huxuehao
 **/
public class InterfaceNotAuthException extends BaseException {
    public InterfaceNotAuthException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public InterfaceNotAuthException(String module, String method, String message) {
        super(ResponseStatus.InterfaceNotAuth.code, module, method, message);
    }

    public InterfaceNotAuthException(String message) {
        super(ResponseStatus.InterfaceNotAuth.code, null, null, message);
    }

    public InterfaceNotAuthException() {
        super(ResponseStatus.InterfaceNotAuth.code, null, null, ResponseStatus.InterfaceNotAuth.msg);
    }
}
