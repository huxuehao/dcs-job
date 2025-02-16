package com.tiger.job.common;

/**
 * 描述：基于Glue脚本实现需要继承的抽象类
 *
 * @author huxuehao
 **/
public abstract class JobHandler {

    public abstract void execute() throws Exception;


    /**
     * 初始化插槽
     */
    public void init() throws Exception {
        // do something
    }


    /**
     * 结束插槽
     */
    public void destroy() throws Exception {
        // do something
    }
}
