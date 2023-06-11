# 分布式动态定时任务-job

#### 介绍
该定时任务是基于springboot的scheduling做的分布式动态定时任务，其功能包括添加定时任务、编辑定时任务、删除定时任务、启动定时任务、关闭定时任务、查看定时执行情况、查看错误日志。目前只提供了后端以及相关的管理接口。注：该项目使用到了Redis。
![输入图片说明](%E5%9B%BE%E7%89%87/image.png)

#### 项目包说明
1. job-admin: 项目启动包。
2. job-api: 定时任务管理接口包。
3. job-server: 服务包，其中包括service、mapper、interceptor、config等。
4. job-core: 项目核心包，包括触发器、扫描器、分布式执行器、心跳检查等。
5. job-common: 项目公共包，包括注解、枚举、实体类、工具包、异常处理、常量等。
6. job-task: 项目定时任务载体包，主要存放用户编写的定时任务。
#### 定时任务编写说明
使用编写定时任务主要依赖于两个注解，即 `@SchedulerBean` 和 `@TaskPath`：
- @SchedulerBean：定时任务类注解，被该注解标记的类将会被定时任务扫描器扫描到。
- @TaskPath：定时任务方法注解，其属性包括 name（定时任务名）、describe（描述）、path（path，必填）、cron（执行策略）、type（分类）、enable（是否启用定时任务）、openLog（是否开启日志采集）

#### 配置文件说明
```yaml
tiger:
  scheduled-task:
    # 扫描类
    scan:
      task-package: com.tiger.job.task # 定时任务的包路径，用于扫描定时任务
      auto-insert: true # 自动扫描报并入库
      default-cron: "0 0/30 * * * ?" # 默认策略
    # 集群配置
    cluster:
      open: true # 开启分布式
    # 日志采集
    log:
      success-open: true # 开启执行成功日志(建议开启)
      fail-open: true # 开启执行失败日志(建议开启)
      save-days: 2 # 日志保存天数
    # 重试策略
    retry:
      open: true # 是否开启重试策略
      count: 1 # 重试次数
      sleep: 5 # 重试间隔（秒）
  # http工具类
  http-util:
    socket-timeout: 60000 # 单位：秒
    connect-timeout: 15000 # 单位：秒
    connection-request-timeout: 60000 # 单位：秒
```