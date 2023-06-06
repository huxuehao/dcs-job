package com.tiger.job.common.exception.member;

import com.tiger.job.common.enums.ResponseStatus;
import com.tiger.job.common.exception.base.BaseException;

/**
 * @ClassName SamePathTaskAPIException
 * @Description TODO
 * @Author huxuehao
 **/
public class SameTaskPathException extends BaseException {

    public SameTaskPathException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public SameTaskPathException(String module, String method, String message) {
        super(ResponseStatus.SamePathTaskPath.code, module, method, message);
    }

    public SameTaskPathException(String message) {
        super(ResponseStatus.SamePathTaskPath.code, null, null, message);
    }

    public SameTaskPathException() {
        super(ResponseStatus.SamePathTaskPath.code, null, null, ResponseStatus.SamePathTaskPath.msg);
    }
}