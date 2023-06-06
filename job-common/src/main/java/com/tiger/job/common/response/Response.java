package com.tiger.job.common.response;

import com.tiger.job.common.enums.ResponseStatus;

import java.util.LinkedHashMap;

/**
 * @ClassName Response
 * @Description TODO
 * @Author huxuehao
 **/
public class Response extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    private static final String CODE_KEY = "code";
    private static final String MSG_KEY = "msg";
    private static final String DATA_KEY = "data";

    public Response() {
    }

    public Response(int code, String msg) {
        super.put(CODE_KEY, code);
        super.put(MSG_KEY, msg);
    }

    public Response(int code, String msg, Object data) {
        super.put(CODE_KEY, code);
        super.put(MSG_KEY, msg);
        super.put(DATA_KEY, data);
    }

    /**
     * @desc 成功响应体
     * @return 封装的返回值
     */
    public static Response success() {
        return Response.success(ResponseStatus.Success.msg);
    }

    /**
     * @desc  成功响应体
     * @param  data 数据
     * @return 封装的返回值
     */
    public static Response success(Object data) {
        return Response.success(ResponseStatus.Success.msg, data);
    }
    /**
     * @desc   成功响应体
     * @param  msg  描述
     * @param  data 数据
     * @return 封装的返回值
     */
    public static Response success(String msg, Object data) {
        return new Response(ResponseStatus.Success.code, msg, data);
    }

    /**
     * @desc 失败响应体
     * @return
     */
    public static Response error() {
        return Response.error(ResponseStatus.Error.msg);
    }

    /**
     * @desc   失败响应体
     * @param  msg 描述
     * @return 封装的返回值
     */
    public static Response error(String msg) {
        return Response.error(ResponseStatus.Error.code, msg);
    }

    /**
     * @desc   失败响应体
     * @param  code 响应码
     * @param  msg 描述
     * @return 封装的返回值
     */
    public static Response error(int code, String msg) {
        return new Response(code, msg);
    }

    /**
     * @desc   失败响应体
     * @param  msg 描述
     * @param  data  数据
     * @return 封装的返回值
     */
    public static Response error(String msg, Object data) {
        return new Response(ResponseStatus.Error.code, msg, data);
    }

    /**
     * @desc   失败响应体
     * @param  code  响应码
     * @param  msg   描述
     * @param  data 数据
     * @return 封装的返回值
     */
    public static Response error(int code, String msg, Object data) {
        return new Response(code, msg, data);
    }

    /**
     * @desc 警告响应体
     * @return 封装的返回值
     */
    public static Response warn() {
        return  Response.warn(ResponseStatus.Warn.msg);
    }

    /**
     * @desc 警告响应体
     * @param msg 描述
     * @return 封装的返回值
     */
    public static Response warn(String msg) {
        return Response.warn(ResponseStatus.Warn.code, msg);
    }

    /**
     * @desc 警告响应体
     * @param msg 描述
     * @param data 数据
     * @return 封装的返回值
     */
    public static Response warn(String msg, Object data) {
        return new Response(ResponseStatus.Warn.code, msg, data);
    }

    /**
     * @desc 警告响应体
     * @param code 响应码
     * @param msg 描述
     * @return 封装的返回值
     */
    public static Response warn(int code, String msg) {
        return new Response(code, msg);
    }

    /**
     * @desc 警告响应体
     * @param code 响应码
     * @param msg 描述
     * @param data 数据
     * @return 封装的返回值
     */
    public static Response warn(int code, String msg, Object data) {
        return new Response(code, msg, data);
    }
}
