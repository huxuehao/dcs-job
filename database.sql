-- 字典表
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` bigint DEFAULT '0' COMMENT '父主键',
  `code` varchar(255) DEFAULT NULL COMMENT '字典码',
  `type` varchar(4) DEFAULT NULL COMMENT '分类',
  `dict_label` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典值',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '字典备注',
  `is_sealed` int DEFAULT '0' COMMENT '是否已封存',
  `del_flag` int DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`)
) COMMENT='字典表';
INSERT INTO `sys_dict`(`id`, `parent_id`, `code`, `type`, `dict_label`, `dict_value`, `sort`, `remark`, `is_sealed`, `del_flag`) VALUES (1657993297488523265, 0, 'schedule_task', '1', '定时任务', NULL, 1, '', 0, 0);
INSERT INTO `sys_dict`(`id`, `parent_id`, `code`, `type`, `dict_label`, `dict_value`, `sort`, `remark`, `is_sealed`, `del_flag`) VALUES (1657994436208504833, 1657993297488523265, 'schedule_task', NULL, '数据抽取定时', 'data_extract_task', 0, '', 0, 0);
INSERT INTO `sys_dict`(`id`, `parent_id`, `code`, `type`, `dict_label`, `dict_value`, `sort`, `remark`, `is_sealed`, `del_flag`) VALUES (1657994436208504836, 1657993297488523265, 'schedule_task', NULL, '生产数据定时', 'production_data_task', 1, '', 0, 0);
INSERT INTO `sys_dict`(`id`, `parent_id`, `code`, `type`, `dict_label`, `dict_value`, `sort`, `remark`, `is_sealed`, `del_flag`) VALUES (1657994436208504837, 1657993297488523265, 'schedule_task', NULL, '数据采集定时', 'data_catch_task', 2, '', 0, 0);
INSERT INTO `sys_dict`(`id`, `parent_id`, `code`, `type`, `dict_label`, `dict_value`, `sort`, `remark`, `is_sealed`, `del_flag`) VALUES (1657994436208504839, 1657993297488523265, 'schedule_task', NULL, '其他定时', 'other_task', 3, '', 0, 0);


-- 用户表
CREATE TABLE `sys_user` (
  `id` varchar(20)  NOT NULL COMMENT '主键',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255)  DEFAULT NULL COMMENT '密码',
  `del_flag` int DEFAULT '0' COMMENT '是否删除（1是 0否）',
  PRIMARY KEY (`id`)
) COMMENT='用户表';
-- user_name 和 password 相同
INSERT INTO `sys_user`(`id`, `user_name`, `password`, `del_flag`) VALUES ('1111111111111111111', 'os-admin', 'e32d7dc9989722834862f517244a7c5a', 0);

-- 定时任务调度表
CREATE TABLE `sys_scheduled` (
  `id` varchar(20) NOT NULL COMMENT 'id',
  `name` varchar(255) DEFAULT NULL COMMENT '定时任务名称',
  `task_type` varchar(100) DEFAULT NULL COMMENT '定时任务分类',
  `task_describe` varchar(255) DEFAULT NULL COMMENT '定时任务描述',
  `cron` varchar(50) NOT NULL COMMENT 'cron策略',
  `path` varchar(500) NOT NULL COMMENT '请求路径',
  `enable` varchar(2) NOT NULL DEFAULT '0' COMMENT '是否启用定时任务',
  `open_log` varchar(2) DEFAULT '0' COMMENT '是否开启日志',
  `create_user` varchar(40) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(40) DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(40) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(40) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_url` (`url`) BLOCK_SIZE 16384 LOCAL
) COMMENT = '定时任务调度表';
INSERT INTO `sys_scheduled`(`id`, `name`, `task_type`, `task_describe`, `cron`, `path`, `enable`, `open_log`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES ('1658314634255069185', '定时任务测试1', 'other_task', '定时任务测试功', '0 0/12 * * * ?', '/sheduled1/execute/do-task1', '0', '1', '1111111111111111111', '2023-05-16 11:33:13', '1111111111111111111', '2023-05-21 17:49:21');
INSERT INTO `sys_scheduled`(`id`, `name`, `task_type`, `task_describe`, `cron`, `path`, `enable`, `open_log`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES ('1658314948605571074', '定时任务测试2', 'other_task', '定时任务测试功', '0 0/15 * * * ?', '/sheduled1/execute/do-task2', '0', '1', '1111111111111111111', '2023-05-16 11:34:28', '1111111111111111111', '2023-05-16 16:22:22');
INSERT INTO `sys_scheduled`(`id`, `name`, `task_type`, `task_describe`, `cron`, `path`, `enable`, `open_log`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES ('1658373657105571841', '定时任务测试3', 'other_task', '定时任务测试功', '0 0/1 * * * ?', '/sheduled2/execute/do-task3', '1', '1', '1111111111111111111', '2023-05-16 15:27:45', '1111111111111111111', '2023-05-24 09:20:04');
INSERT INTO `sys_scheduled`(`id`, `name`, `task_type`, `task_describe`, `cron`, `path`, `enable`, `open_log`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES ('1658380167663624193', '定时任务测试4', 'other_task', '定时任务测试功', '0 0/3 * * * ?', '/sheduled2/execute/do-task4', '0', '1', '1111111111111111111', '2023-05-16 15:53:37', '1111111111111111111', '2023-05-24 09:20:04');
INSERT INTO `sys_scheduled`(`id`, `name`, `task_type`, `task_describe`, `cron`, `path`, `enable`, `open_log`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES ('1658380313612820482', '定时任务测试5', 'other_task', '定时任务测试功', '0 0/2 * * * ?', '/topic/execute/do-task1', '0', '1', '1111111111111111111', '2023-05-16 15:54:12', '1111111111111111111', '2023-05-24 09:20:04');

-- 定时任务调度日志表
CREATE TABLE `sys_scheduled_log` (
  `id` varchar(20) NOT NULL COMMENT 'id',
  `task_id` varchar(20) DEFAULT NULL COMMENT '定时任务ID',
  `task_name` varchar(255) DEFAULT NULL COMMENT '定时任务名称',
  `execute_status` varchar(40) DEFAULT NULL COMMENT '执行状态',
  `content` longtext DEFAULT NULL COMMENT '内容',
  `execute_time` varchar(40) DEFAULT NULL COMMENT '执行时间',
  PRIMARY KEY (`id`)
) COMMENT = '定时任务调度日志表';
