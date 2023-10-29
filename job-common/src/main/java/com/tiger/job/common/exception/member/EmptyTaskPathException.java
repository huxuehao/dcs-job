package com.tiger.job.common.exception.member;

import com.tiger.job.common.enums.ResponseStatus;
import com.tiger.job.common.exception.base.BaseException;

/**
 * 描述：存在path为空的TaskPath注解
 *
 * @author huxuehao
 **/
public class EmptyTaskPathException  extends BaseException {

    public EmptyTaskPathException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public EmptyTaskPathException(String module, String method, String message) {
        super(ResponseStatus.EmptyTaskPath.code, module, method, message);
    }

    public EmptyTaskPathException(String message) {
        super(ResponseStatus.EmptyTaskPath.code, null, null, message);
    }

    public EmptyTaskPathException() {
        super(ResponseStatus.EmptyTaskPath.code, null, null, ResponseStatus.EmptyTaskPath.msg);
    }
}
