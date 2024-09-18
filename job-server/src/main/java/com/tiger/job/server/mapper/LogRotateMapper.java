package com.tiger.job.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * mapper
 *
 * @author huxuehao
 **/
@Mapper
public interface LogRotateMapper {
    /* 查询日志表中日周期 */
    String selectDayCycle();

    /* 复制表，实现日志轮转 */
    int copyTable(@Param("tableName_n") String tableName_n);

    /* 如果存在tableName_n表，则清空日志表的日志 */
    int delRecordsIFExist(@Param("tableName_n") String tableName_n);
}
