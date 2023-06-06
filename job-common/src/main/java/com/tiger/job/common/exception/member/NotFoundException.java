package com.tiger.job.common.exception.member;

import com.tiger.job.common.enums.ResponseStatus;
import com.tiger.job.common.exception.base.BaseException;

/**
 * @ClassName NotFoundException
 * @Description 未找到异常
 * @Author StudiousTiger
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
