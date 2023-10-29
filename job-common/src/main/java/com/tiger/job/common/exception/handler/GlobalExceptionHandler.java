package com.tiger.job.common.exception.handler;

import com.tiger.job.common.exception.member.*;
import com.tiger.job.common.response.Response;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

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
    public Response exceptionHandler(Exception e) {
        e.printStackTrace();
        return Response.error(500, "系统内部错误");
    }
    @ResponseBody
    @Description(value = "空指针异常")
    @ExceptionHandler(value = NullPointerException.class)
    public Response exceptionHandler(NullPointerException e) {
        e.printStackTrace();
        return Response.error(500, "空指针异常");
    }

    @ResponseBody
    @Description(value = "SQL异常处理")
    @ExceptionHandler(value = SQLException.class)
    public Response exceptionHandler(SQLException e) {
        e.printStackTrace();
        return Response.error(500, e.getMessage());
    }

    @ResponseBody
    @Description(value = "内部系统错误异常处理")
    @ExceptionHandler(value = ErrorException.class)
    public Response exceptionHandler(ErrorException e) {
        return Response.error(500, e.getMessage());
    }

    @ResponseBody
    @Description(value = "接口没有进行权限标记异常处理")
    @ExceptionHandler(value = InterfaceNotAuthException.class)
    public Response exceptionHandler(InterfaceNotAuthException e) {
        return Response.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "未找到异常处理")
    @ExceptionHandler(value = NotFoundException.class)
    public Response exceptionHandler(NotFoundException e) {
        return Response.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "权限异常处理")
    @ExceptionHandler(value = NotAuthException.class)
    public Response exceptionHandler(NotAuthException e) {
        return Response.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "请求超时异常处理")
    @ExceptionHandler(value = TimeoutException.class)
    public Response exceptionHandler(TimeoutException e) {
        return Response.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "不是文件夹的异常处理")
    @ExceptionHandler(value = NotFolderException.class)
    public Response exceptionHandler(NotFolderException e) {
        return Response.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "存在相同path的TaskPath注解")
    @ExceptionHandler(value = SameTaskPathException.class)
    public Response exceptionHandler(SameTaskPathException e) {
        return Response.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "存在path为空的TaskPath注解")
    @ExceptionHandler(value = EmptyTaskPathException.class)
    public Response exceptionHandler(EmptyTaskPathException e) {
        return Response.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @Description(value = "未匹配到有效的path")
    @ExceptionHandler(value = NotMatchPathException.class)
    public Response exceptionHandler(NotMatchPathException e) {
        return Response.error(e.getCode(), e.getMessage());
    }


}
