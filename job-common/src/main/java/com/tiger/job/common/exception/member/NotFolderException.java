package com.tiger.job.common.exception.member;

import com.tiger.job.common.enums.ResponseStatus;
import com.tiger.job.common.exception.base.BaseException;

/**
 * 描述：非文件夹异常
 *
 * @author huxuehao
 **/
public class NotFolderException extends BaseException {
    public NotFolderException(int code, String module, String method, String message) {
        super(code, module, method, message);
    }

    public NotFolderException(String module, String method, String message) {
        super(ResponseStatus.NotFolder.code, module, method, message);
    }

    public NotFolderException(String message) {
        super(ResponseStatus.NotFolder.code, null, null, message);
    }

    public NotFolderException() {
        super(ResponseStatus.NotFolder.code, null, null, ResponseStatus.NotFolder.msg);
    }
}