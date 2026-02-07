package com.hxh.job.common.exception.handler;

import com.hxh.job.common.exception.member.*;
import com.hxh.job.common.r.R;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述：全局异常处理
 *
 * @author huxuehao
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @Description(value = "内部错误")
    @ExceptionHandler(value = Exception.class)
    public R<?> exceptionHandler(Exception e) {
        e.printStackTrace();

        Throwable cause = e.getCause();
        if (cause != null) {
            return R.fail(500, cause.getMessage());
        } else {
            return R.fail(500, e.getMessage());
        }
    }
    @ResponseBody
    @Description(value = "空指针异常")
    @ExceptionHandler(value = NullPointerException.class)
    public R<?> exceptionHandler(NullPointerException e) {
        e.printStackTrace();
        return R.fail(500, "空指针异常");
    }

    @ResponseBody
    @Description(value = "内部系统错误异常处理")
    @ExceptionHandler(value = ErrorException.class)
    public R<?> exceptionHandler(ErrorException e) {
        return R.fail(500, e.getMessage());
    }

    @ResponseBody
    @Description(value = "接口没有进行权限标记异常处理")
    @ExceptionHandler(value = InterfaceNotAuthException.class)
    public R<?> exceptionHandler(InterfaceNotAuthException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "未找到异常处理")
    @ExceptionHandler(value = NotFoundException.class)
    public R<?> exceptionHandler(NotFoundException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "权限异常处理")
    @ExceptionHandler(value = NotAuthException.class)
    public R<?> exceptionHandler(NotAuthException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "请求超时异常处理")
    @ExceptionHandler(value = TimeoutException.class)
    public R<?> exceptionHandler(TimeoutException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "不是文件夹的异常处理")
    @ExceptionHandler(value = NotFolderException.class)
    public R<?> exceptionHandler(NotFolderException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "存在相同path的TaskPath注解")
    @ExceptionHandler(value = SameTaskPathException.class)
    public R<?> exceptionHandler(SameTaskPathException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "存在path为空的TaskPath注解")
    @ExceptionHandler(value = EmptyTaskPathException.class)
    public R<?> exceptionHandler(EmptyTaskPathException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "未匹配到有效的path")
    @ExceptionHandler(value = NotMatchPathException.class)
    public R<?> exceptionHandler(NotMatchPathException e) {
        return R.fail(e.getCode(), e.getMessage());
    }


}
