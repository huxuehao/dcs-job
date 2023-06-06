package com.tiger.job.common.exception.member;

import com.tiger.job.common.enums.ResponseStatus;
import com.tiger.job.common.exception.base.BaseException;

/**
 * @ClassName MatchPathException
 * @Description TODO
 * @Author huxuehao
 **/
public class NotMatchPathException extends BaseException {
    public NotMatchPathException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public NotMatchPathException(String module, String method, String message) {
        super(ResponseStatus.NotMatchPath.code, module, method, message);
    }

    public NotMatchPathException(String message) {
        super(ResponseStatus.NotMatchPath.code, null, null, message);
    }

    public NotMatchPathException() {
        super(ResponseStatus.NotMatchPath.code, null, null, ResponseStatus.NotMatchPath.msg);
    }
}