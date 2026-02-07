package com.hxh.job.core.executor;

import com.hxh.job.common.constant.ClusterProperties;
import com.hxh.job.core.executor.impl.ClusterExecutor;
import com.hxh.job.core.executor.impl.SingleExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 描述：执行适配器
 *
 * @author huxuehao
 **/
@Component
public class AdapterExecutor {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ClusterProperties clusterProperties;
    private final ClusterExecutor clusterExecutor;
    private final SingleExecutor singleExecutor;

    public AdapterExecutor(ClusterProperties clusterProperties, ClusterExecutor clusterExecutor, SingleExecutor singleExecutor) {
        this.clusterProperties = clusterProperties;
        this.clusterExecutor = clusterExecutor;
        this.singleExecutor = singleExecutor;
    }

    public Executor matchExecutor() {
        log.info("定时任务：当前定时任务为[{}]", clusterProperties.isOpen()? "集群模式" : "单例模式");
        return clusterProperties.isOpen()? clusterExecutor : singleExecutor;
    }

}
