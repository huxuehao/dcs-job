package com.tiger.job.common.response;

import com.tiger.job.common.enums.ResponseStatus;

import java.util.LinkedHashMap;

/**
 * 描述：响应体
 *
 * @author huxuehao
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
     * 成功响应体
     * @return 封装的返回值
     */
    public static Response success() {
        return Response.success(ResponseStatus.Success.msg);
    }

    /**
     * 成功响应体
     * @param  data 数据
     * @return 封装的返回值
     */
    public static Response success(Object data) {
        return Response.success(ResponseStatus.Success.msg, data);
    }
    /**
     * 成功响应体
     * @param  msg  描述
     * @param  data 数据
     * @return 封装的返回值
     */
    public static Response success(String msg, Object data) {
        return new Response(ResponseStatus.Success.code, msg, data);
    }

    /**
     * 失败响应体
     */
    public static Response error() {
        return Response.error(ResponseStatus.Error.msg);
    }

    /**
     * 失败响应体
     * @param  msg 描述
     * @return 封装的返回值
     */
    public static Response error(String msg) {
        return Response.error(ResponseStatus.Error.code, msg);
    }

    /**
     * 失败响应体
     * @param  code 响应码
     * @param  msg 描述
     * @return 封装的返回值
     */
    public static Response error(int code, String msg) {
        return new Response(code, msg);
    }

    /**
     * 失败响应体
     * @param  msg 描述
     * @param  data  数据
     * @return 封装的返回值
     */
    public static Response error(String msg, Object data) {
        return new Response(ResponseStatus.Error.code, msg, data);
    }

    /**
     * 失败响应体
     * @param  code  响应码
     * @param  msg   描述
     * @param  data 数据
     * @return 封装的返回值
     */
    public static Response error(int code, String msg, Object data) {
        return new Response(code, msg, data);
    }

    /**
     * 警告响应体
     * @return 封装的返回值
     */
    public static Response warn() {
        return  Response.warn(ResponseStatus.Warn.msg);
    }

    /**
     * 警告响应体
     * @param msg 描述
     * @return 封装的返回值
     */
    public static Response warn(String msg) {
        return Response.warn(ResponseStatus.Warn.code, msg);
    }

    /**
     * 警告响应体
     * @param msg 描述
     * @param data 数据
     * @return 封装的返回值
     */
    public static Response warn(String msg, Object data) {
        return new Response(ResponseStatus.Warn.code, msg, data);
    }

    /**
     * 警告响应体
     * @param code 响应码
     * @param msg 描述
     * @return 封装的返回值
     */
    public static Response warn(int code, String msg) {
        return new Response(code, msg);
    }

    /**
     * 警告响应体
     * @param code 响应码
     * @param msg 描述
     * @param data 数据
     * @return 封装的返回值
     */
    public static Response warn(int code, String msg, Object data) {
        return new Response(code, msg, data);
    }
}
