package com.tiger.job.common.enums;

/**
 * @EnumName ResponseStatus
 * @Description 响应体
 * @Author huxuehao
 **/
public enum ResponseStatus {
    Success(200, "请求成功"),
    NoContent(204, "没有内容"),

    BadRequest(400, "错误请求"),
    NotAuth(401, "未经授权"),
    InterfaceNotAuth(402, "接口没有进行权限标记"),
    NotMatchPath(403, "未匹配到有效的path"),
    NotFound(404, "没有找到"),
    NotFolder(405, "扫描路径不是文件夹"),
    Timeout(408, "请求超时"),

    Error(500, "系统内部错误"),
    SamePathTaskPath(501, "存在相同path的TaskPath注解"),
    EmptyTaskPath(503, "存在path为空的TaskPath注解"),
    DataSourceNotFound(505, "未找到目标数据源"),
    DataSourceCreateError(506, "数据源创建失败"),
    DataSourceConfigError(507, "数据源配置存在错误"),
    NotGetDsAnnotation(508, "使用了ChangeDS注解，但是未找到Datasource标记的参数"),

    Warn(600, "操作异常警告");

    public int code;
    public String msg;

    ResponseStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code()
    {
        return code;
    }

    public String msg()
    {
        return msg;
    }
}
