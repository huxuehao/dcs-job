package com.hxh.job.common.r;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author huxuehao
 */
public interface IResultCode extends Serializable {
    String getMessage();

    int getCode();
}
