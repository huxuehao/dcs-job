SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_classify
-- ----------------------------
DROP TABLE IF EXISTS `sys_classify`;
CREATE TABLE `sys_classify`  (
                                 `id` bigint(0) NOT NULL COMMENT '主键',
                                 `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                                 `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
                                 `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                 `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                                 `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                                 `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_classify
-- ----------------------------
INSERT INTO `sys_classify` VALUES (1890212284891422722, 0, 'default', '默认定时', NULL, 0, 0);
INSERT INTO `sys_classify` VALUES (1890212463992397826, 0, 'person', '人资类定时', NULL, 1, 0);
INSERT INTO `sys_classify` VALUES (1890212597069275137, 0, 'data', '数据抽取', NULL, 2, 0);
INSERT INTO `sys_classify` VALUES (1890212824727707650, 0, 'system', '系统定时', NULL, 3, 0);
INSERT INTO `sys_classify` VALUES (1890212845476929538, 1890212463992397826, '1', '1', NULL, 1, 1);

-- ----------------------------
-- Table structure for sys_glue_version
-- ----------------------------
DROP TABLE IF EXISTS `sys_glue_version`;
CREATE TABLE `sys_glue_version`  (
                                     `task_id` bigint(0) NULL DEFAULT NULL COMMENT '任务ID',
                                     `config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '配置',
                                     `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
                                     `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                                     `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                     `create_time` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'glue版本控制表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_glue_version
-- ----------------------------
INSERT INTO `sys_glue_version` VALUES (1890663872508481538, 'import com.tiger.job.common.JobHandler;\n\npublic class Demo extends JobHandler {\n\n    @Override\n    public void execute() throws Exception {\n        System.out.println(\"do something...\");\n    }\n}', '初始化', 'JAVA', '1111111111111111111', '2025-02-15 22:29:59');
INSERT INTO `sys_glue_version` VALUES (1890663872508481538, 'import com.tiger.job.common.JobHandler;\n\npublic class Demo extends JobHandler {\n\n    @Override\n    public void execute() throws Exception {\n        System.out.println(\"do something....\");\n    }\n}', '1', 'JAVA', '1111111111111111111', '2025-02-15 22:39:35');
INSERT INTO `sys_glue_version` VALUES (1890663872508481538, 'import com.tiger.job.common.JobHandler;\n\npublic class Demo extends JobHandler {\n\n    @Override\n    public void execute() throws Exception {\n        System.out.println(\"do something.....\");\n    }\n}', '..', 'JAVA', '1111111111111111111', '2025-02-15 22:39:49');
INSERT INTO `sys_glue_version` VALUES (1890663872508481538, 'import com.tiger.job.common.JobHandler;\n\npublic class Demo extends JobHandler {\n\n    @Override\n    public void execute() throws Exception {\n        System.out.println(\"do something...\");\n    }\n}', '载入测试', 'JAVA', '1111111111111111111', '2025-02-15 22:59:48');
INSERT INTO `sys_glue_version` VALUES (1890663872508481538, 'import com.tiger.job.common.JobHandler;\nimport com.tiger.job.task.topic.ComponentBean;\nimport org.springframework.beans.factory.annotation.Autowired;\n\npublic class Demo extends JobHandler {\n    @Autowired\n    private ComponentBean componentBean;\n  \n    @Override\n    public void execute() throws Exception {\n        System.out.println(\"do something...\");\n        componentBean.invoke();\n    }\n}', '添加spring的bean测试注入情况', 'JAVA', '1111111111111111111', '2025-02-15 23:04:49');
INSERT INTO `sys_glue_version` VALUES (1891035278350082049, 'import com.tiger.job.common.JobHandler;\n\npublic class Demo extends JobHandler {\n\n    @Override\n    public void execute() throws Exception {\n        throw new RuntimeException(\"错误日志查看测试\");\n    }\n}', 'init', 'JAVA', '1111111111111111111', '2025-02-16 16:08:56');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `id` bigint(0) NOT NULL COMMENT 'ID',
                             `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                             `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
                             `icon` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
                             `alias` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单别名',
                             `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问地址',
                             `params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
                             `code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单编号',
                             `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                             `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                             `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1873718566239940609, 0, '系统管理', 'Tools', '系统管理', '/system', '[]', 'system', 1, 3, 0);
INSERT INTO `sys_menu` VALUES (1873718825632477186, 1873718566239940609, '组织机构', 'MapLocation', '组织机构', '/system/org', '[]', 'system_org', 1, 1, 0);
INSERT INTO `sys_menu` VALUES (1873718995677949953, 1873718566239940609, '用户管理', 'User', '用户管理', '/system/user', '[]', 'system_user', 1, 2, 0);
INSERT INTO `sys_menu` VALUES (1873719239731916802, 1873718566239940609, '系统菜单', 'Menu', '系统菜单', '/system/menu', '[]', 'system_menu', 1, 9, 0);
INSERT INTO `sys_menu` VALUES (1880186212221272065, 1873718566239940609, '系统参数', 'Discount', '系统参数', '/system/params', '[]', 'system_params', 1, 5, 0);
INSERT INTO `sys_menu` VALUES (1884974021100544001, 1884975346991341570, '按钮管理', 'Pointer', '按钮管理', '/system/btnmanager', '[]', 'system_btnmanager', 1, 5, 0);
INSERT INTO `sys_menu` VALUES (1884974216035016705, 1884975346991341570, '接口管理', 'Connection', '接口管理', '/system/apimanager', '[]', 'system_apimanager', 1, 6, 0);
INSERT INTO `sys_menu` VALUES (1884974935785971713, 1884975346991341570, '角色管理', 'User', '角色管理', '/system/rolemanager', '[]', 'system_rolemanager', 1, 7, 0);
INSERT INTO `sys_menu` VALUES (1884975346991341570, 1873718566239940609, '系统权限', 'Lock', '系统权限', '/authmanager', '[]', 'authmanager', 1, 3, 0);
INSERT INTO `sys_menu` VALUES (1887852869190205441, 0, '首页', 'HomeFilled', '首页', '/home', '[]', 'home', 1, -1, 0);
INSERT INTO `sys_menu` VALUES (1890050211435892738, 0, '定时任务', 'HelpFilled', '定时任务', '/job', '[]', 'job', 1, 1, 0);
INSERT INTO `sys_menu` VALUES (1890050634607611905, 1890050211435892738, '任务分类', 'Operation', '任务分类', '/job/classify', NULL, 'job_classify', 1, 1, 0);
INSERT INTO `sys_menu` VALUES (1890050839264481281, 1890050211435892738, '任务实例', 'Clock', '任务实例', '/job/task', NULL, 'job_task', 1, 2, 0);
INSERT INTO `sys_menu` VALUES (1890051054755237890, 1890050211435892738, '任务日志', 'Tickets', '任务日志', '/job/log', NULL, 'job_log', 1, 3, 0);

-- ----------------------------
-- Table structure for sys_menu_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_api`;
CREATE TABLE `sys_menu_api`  (
                                 `id` bigint(0) NOT NULL COMMENT '主键',
                                 `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
                                 `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口名称',
                                 `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口编号',
                                 `path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口路径',
                                 `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口方法',
                                 `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                                 `type` int(0) NULL DEFAULT NULL COMMENT '类型（1自动扫描，0手动添加）',
                                 `next_time_millis` bigint(0) NULL DEFAULT NULL COMMENT '上次操作的时间戳',
                                 `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单接口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu_api
-- ----------------------------
INSERT INTO `sys_menu_api` VALUES (1887368830901669889, 1884974216035016705, '新增', 'post::menu-api:add', '/menu-api/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368830901669890, 1884974216035016705, '编辑', 'post::menu-api:update', '/menu-api/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368830972973057, 1884974216035016705, '删除', 'post::menu-api:delete', '/menu-api/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368830972973058, 1884974216035016705, '分页', 'get::menu-api:page', '/menu-api/page', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368830972973059, 1884974216035016705, '设置有效', 'post::menu-api:valid', '/menu-api/valid', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368830972973060, 1884974216035016705, '根据ID唯一获取', 'get::menu-api:selectOne', '/menu-api/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831040081922, 1884974216035016705, '设置无效', 'post::menu-api:unValid', '/menu-api/unValid', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831040081923, 1884974021100544001, '新增', 'post::menu-button:add', '/menu-button/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831040081924, 1884974021100544001, '编辑', 'post::menu-button:update', '/menu-button/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831102996481, 1884974021100544001, '删除', 'post::menu-button:delete', '/menu-button/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831102996482, 1884974021100544001, '分页', 'get::menu-button:page', '/menu-button/page', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831102996483, 1884974021100544001, '设置有效', 'post::menu-button:valid', '/menu-button/valid', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831102996484, 1884974021100544001, '根据ID唯一获取', 'get::menu-button:selectOne', '/menu-button/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831165911041, 1884974021100544001, '设置无效', 'post::menu-button:unValid', '/menu-button/unValid', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831165911042, 1884974935785971713, '新增', 'post::role:add', '/role/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831165911043, 1884974935785971713, '编辑', 'post::role:update', '/role/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831233019905, 1884974935785971713, '删除', 'post::role:delete', '/role/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831233019906, 1884974935785971713, '树形列表', 'get::role:tree', '/role/tree', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831233019907, 1884974935785971713, '根据ID唯一获取', 'get::role:selectOne', '/role/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831698587649, 1873719239731916802, '新增', 'post::menu:add', '/menu/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831698587650, 1873719239731916802, '编辑', 'post::menu:update', '/menu/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831698587651, 1873719239731916802, '设置有效接口', 'post::menu:set-valid', '/menu/set-valid', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831698587652, 1873719239731916802, '删除', 'post::menu:delete', '/menu/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831765696513, 1873719239731916802, '列表', 'get::menu:list', '/menu/list', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831765696514, 1873719239731916802, '树形列表', 'get::menu:tree', '/menu/tree', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831765696515, 1873719239731916802, '根据ID唯一获取', 'get::menu:selectOne', '/menu/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831765696516, 1873718825632477186, '新增', 'post::org:add', '/org/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831832805377, 1873718825632477186, '编辑', 'post::org:update', '/org/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831849582594, 1873718825632477186, '删除', 'post::org:delete', '/org/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831862165505, 1873718825632477186, '树形列表', 'get::org:tree', '/org/tree', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368831862165506, 1873718825632477186, '根据ID唯一获取', 'get::org:selectOne', '/org/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368832159961089, 1880186212221272065, '新增', 'post::params:add', '/params/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368832159961090, 1880186212221272065, '编辑', 'post::params:update', '/params/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368832159961091, 1880186212221272065, '删除', 'post::params:delete', '/params/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368832159961092, 1880186212221272065, '根据key获取value', 'get::params:fetch-value-by-key', '/params/fetch-value-by-key', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368832222875650, 1880186212221272065, '根据ID唯一获取', 'get::params:selectOne', '/params/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368832222875651, 1880186212221272065, '分页', 'get::params:page', '/params/page', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833082707972, 1873718995677949953, '新增', 'post::user:add', '/user/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833082707973, 1873718995677949953, '更新', 'post::user:update', '/user/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833145622529, 1873718995677949953, '删除', 'post::user:delete', '/user/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833145622530, 1873718995677949953, '分页', 'get::user:page', '/user/page', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833145622531, 1873718995677949953, '解封账号', 'post::user:valid', '/user/valid', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833145622532, 1873718995677949953, '根据ID集合批量获取', 'post::user:selectByIds', '/user/selectByIds', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833145622533, 1873718995677949953, '登录', 'post::user:login', '/user/login', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833212731393, 1873718995677949953, '退出', 'get::user:logout', '/user/logout', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833212731394, 1873718995677949953, '刷新Token', 'get::user:refresh-token', '/user/refresh-token', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833212731395, 1873718995677949953, '重置密码', 'post::user:resetPwd', '/user/resetPwd', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887368833212731396, 1873718995677949953, '根据ID唯一获取', 'get::user:selectOne', '/user/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887386211451527169, 1884974021100544001, '菜单按钮树形列表', 'get::menu-button:menu-button-tree', '/menu-button/menu-button-tree', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887409368325550081, 1884974216035016705, '菜单接口树形列表', 'get::menu-api:menu-button-tree', '/menu-api/menu-button-tree', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887409368719814657, 1884974935785971713, '保存权限配置', 'post::role:save-auth-config', '/role/save-auth-config', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887409368719814658, 1884974935785971713, '根据RoleId获取权限配置', 'get::role:get-auth-config', '/role/get-auth-config', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887428167535693825, 1884974935785971713, '保存用户角色', 'post::role:save-user-role', '/role/save-user-role', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887428167535693826, 1884974935785971713, '根据UserId获取用户角色', 'get::role:get-user-role', '/role/get-user-role', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887519037974904833, 1884974935785971713, '根据用户按钮权限', 'get::role:get-button-permissions', '/role/get-button-permissions', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887686303433732098, 1873719239731916802, '用户树形列表权限', 'get::menu:permission-tree', '/menu/permission-tree', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1887773594084782082, 1884974216035016705, '接口采集/校验', 'get::endpoint:scan-and-save-api', '/endpoint/scan-and-save-api', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1888096197693534210, 1887852869190205441, '用户活跃次数统计', 'get::statistics:user-active', '/statistics/user-active', 'GET', 1, 1, 1739091672908, 1);
INSERT INTO `sys_menu_api` VALUES (1888096197743865858, 1887852869190205441, '题目类型统计', 'get::statistics:question-type', '/statistics/question-type', 'GET', 1, 1, 1739091672908, 1);
INSERT INTO `sys_menu_api` VALUES (1888096197743865859, 1887852869190205441, '题目难度统计', 'get::statistics:question-level', '/statistics/question-level', 'GET', 1, 1, 1739091672908, 1);
INSERT INTO `sys_menu_api` VALUES (1888096197743865860, 1887852869190205441, '考试培训状态统计', 'get::statistics:exam-train-status', '/statistics/exam-train-status', 'GET', 1, 1, 1739091672908, 1);
INSERT INTO `sys_menu_api` VALUES (1888504503339683842, 1873718995677949953, '获取当前用户的用户信息', 'get::user:current-user-info', '/user/current-user-info', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1888513473714442241, 1873718995677949953, '重置登录密码', 'post::user:re-pwd', '/user/re-pwd', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1889344835568640001, 1887852869190205441, '解锁全部的定时任务锁', 'get::unlock:task-lock-all', '/unlock/task/lock/all', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1889344835568640002, 1887852869190205441, '解锁日志轮转定时任务锁', 'get::unlock:task-lock-rotate', '/unlock/task/lock/rotate', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1889344835568640003, 1887852869190205441, '根据定时任务id解锁，定时任务锁', 'get::unlock:task-lock-by-id', '/unlock/task/lock/by-id', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890211169894510594, 1890050634607611905, '新增', 'post::job:classify:add', '/job/classify/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890211169919676417, 1890050634607611905, '编辑', 'post::job:classify:update', '/job/classify/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890211169919676418, 1890050634607611905, '删除', 'post::job:classify:delete', '/job/classify/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890211169919676419, 1890050634607611905, '树形列表', 'get::job:classify:tree', '/job/classify/tree', 'GET', 1, 1, 1739496435956, 1);
INSERT INTO `sys_menu_api` VALUES (1890211169919676420, 1890050634607611905, '根据ID唯一获取', 'get::job:classify:selectOne', '/job/classify/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890211170003562497, 1890050839264481281, '执行任务', 'get::job:task:execute', '/job/task/execute', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890212115076636673, 1890050634607611905, '树形列表', 'get::job:classify:tree', '/job/classify/tree', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487271219201, 1890051054755237890, '下一条日志', 'get::job:log:next', '/job/log/next', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487271219202, 1890051054755237890, '分页', 'get::job:log:page', '/job/log/page', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487271219203, 1890051054755237890, '根据ID唯一获取', 'get::job:log:selectOne', '/job/log/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487325745153, 1890051054755237890, '获取最新的错误日志', 'get::job:log:latest-by-task-id', '/job/log/latest-by-task-id', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487338328066, 1890051054755237890, '上一条日志', 'get::job:log:last', '/job/log/last', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487355105282, 1890050839264481281, '新增', 'post::job:task:add', '/job/task/add', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487380271105, 1890050839264481281, '启动任务', 'post::job:task:enable', '/job/task/enable', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487380271106, 1890050839264481281, '根据ID唯一获取', 'get::job:task:selectOne', '/job/task/selectOne', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487380271107, 1890050839264481281, '暂停任务', 'post::job:task:disable', '/job/task/disable', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487380271108, 1890050839264481281, '列表', 'get::job:task:list', '/job/task/list', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487451574274, 1890050839264481281, '分页', 'get::job:task:page', '/job/task/page', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487451574275, 1890050839264481281, '刷新执行结果', 'get::job:task:refresh-result', '/job/task/refresh-result', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487451574276, 1890050839264481281, '编辑', 'post::job:task:update', '/job/task/update', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890236487451574277, 1890050839264481281, '删除任务', 'post::job:task:delete', '/job/task/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890327108988289025, 1890050839264481281, '未来执行计划', 'get::job:task:future-execution-plan', '/job/task/future-execution-plan', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1890767432386650113, 1890050839264481281, 'Glue保存', 'post::job:task:save', '/job/glue/save', 'POST', 1, 1, 1739693977810, 1);
INSERT INTO `sys_menu_api` VALUES (1890767432424398850, 1890050839264481281, 'Glue列表', 'get::job:task:list-by-task-id', '/job/glue/list-by-task-id', 'GET', 1, 1, 1739693977810, 1);
INSERT INTO `sys_menu_api` VALUES (1890767432424398851, 1890050839264481281, 'Glue保存并发布', 'post::job:task:save-publish', '/job/glue/save-publish', 'POST', 1, 1, 1739693977810, 1);
INSERT INTO `sys_menu_api` VALUES (1891030824393494530, 1890051054755237890, '删除', 'get::job:log:delete', '/job/log/delete', 'GET', 1, 1, 1739691856766, 1);
INSERT INTO `sys_menu_api` VALUES (1891039720671649793, 1890051054755237890, '删除', 'post::job:log:delete', '/job/log/delete', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1891044868634554369, 1890050839264481281, 'Glue保存', 'post::job:glue:save', '/job/glue/save', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1891044868647137281, 1890050839264481281, 'Glue保存并发布', 'post::job:glue:save-publish', '/job/glue/save-publish', 'POST', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1891044868714246145, 1890050839264481281, 'Glue列表', 'get::job:glue:list-by-task-id', '/job/glue/list-by-task-id', 'GET', 1, 1, 1739695205228, 0);
INSERT INTO `sys_menu_api` VALUES (1891044868714246146, 1890050839264481281, '根据标记唯一获取', 'get::job:glue:selectOne', '/job/glue/selectOne', 'GET', 1, 1, 1739695205228, 0);

-- ----------------------------
-- Table structure for sys_menu_button
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_button`;
CREATE TABLE `sys_menu_button`  (
                                    `id` bigint(0) NOT NULL COMMENT '主键',
                                    `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
                                    `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮名称',
                                    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮编号',
                                    `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                                    `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单按钮表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu_button
-- ----------------------------
INSERT INTO `sys_menu_button` VALUES (1887060807440228354, 1873662796710297602, '查看', 'task_view', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887060880920240129, 1873662796710297602, '操作', 'task_opt', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887062629131337729, 1873663010963734530, '新增', 'question_add', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887062692809261057, 1873663010963734530, '编辑', 'question_edit', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887062738749472770, 1873663010963734530, '查看', 'question_view', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887062796966412290, 1873663010963734530, '删除', 'question_delete', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887062904395120641, 1873663010963734530, '批量导入', 'question_import', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063111069450242, 1873663010963734530, '分类管理', 'question_classify', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063179793121282, 1873663010963734530, '预览', 'question_preview', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063459171516418, 1873665587134611458, '新增', 'paper_add', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063504042180609, 1873665587134611458, '编辑', 'paper_edit', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063541224685569, 1873665587134611458, '删除', 'paper_delete', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063598833451009, 1873665587134611458, '预览', 'paper_preview', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063662704312321, 1873665587134611458, '分类管理', 'paper_classify', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063776751632386, 1873717926629552129, '新增', 'exam_add', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063848511979522, 1873717926629552129, '编辑', 'exam_edit', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887063940761501698, 1873717926629552129, '删除', 'exam_delete', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064016795844610, 1873717926629552129, '预览', 'exam_preview', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064064778682369, 1873717926629552129, '查看', 'exam_view', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064280571428865, 1873717926629552129, '发布/撤销', 'exam_publish', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064412964634626, 1873717926629552129, '分类管理', 'exam_classify', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064695383900162, 1873717926629552129, '进展情况', 'exam_progress', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064794554023937, 1873718395699539970, '新增', 'train_add', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064832663470081, 1873718395699539970, '编辑', 'train_edit', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064863403524097, 1873718395699539970, '删除', 'train_delete', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064911596077058, 1873718395699539970, '查看', 'train_view', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887064947889389569, 1873718395699539970, '预览', 'train_preview', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887065063341801473, 1873718395699539970, '发布/撤销', 'train_publish', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887065133713833986, 1873718395699539970, '分类管理', 'train_classify', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887065182485200897, 1873718395699539970, '进展情况', 'train_progress', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887065339096317954, 1873718825632477186, '新增', 'org_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065378925428737, 1873718825632477186, '编辑', 'org_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065426224594946, 1873718825632477186, '删除', 'org_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065457656709122, 1873718825632477186, '查看', 'org_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065597855514625, 1873718825632477186, '新增子项', 'org_add_child', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065657364299778, 1873718995677949953, '新增', 'user_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065708857769985, 1873718995677949953, '编辑', 'user_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065737819439106, 1873718995677949953, '删除', 'user_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065781016576002, 1873718995677949953, '查看', 'user_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065892278878210, 1873718995677949953, '角色配置', 'user_role', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887065958897008642, 1873718995677949953, '重置密码', 'user_repassword', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066121455648770, 1873718995677949953, '解封账号', 'user_valid', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066272765165569, 1884974021100544001, '新增', 'btn_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066321058381825, 1884974021100544001, '编辑', 'btn_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066361856376834, 1884974021100544001, '删除', 'btn_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066407792394241, 1884974021100544001, '查看', 'btn_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066516273872897, 1884974021100544001, '设置有效', 'btn_valid', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066596259250178, 1884974021100544001, '设置无效', 'btn_unvalid', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066754980102146, 1884974216035016705, '新增', 'api_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066809317310465, 1884974216035016705, '编辑', 'api_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066852942266370, 1884974216035016705, '删除', 'api_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066888501575682, 1884974216035016705, '查看', 'api_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066942725537794, 1884974216035016705, '设置有效', 'api_valid', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887066997234712577, 1884974216035016705, '设置无效', 'api_unvalid', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887067172544036865, 1884974216035016705, '接口采集/校验', 'api_collection', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887067731896418305, 1884974935785971713, '新增', 'role_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887067767917101058, 1884974935785971713, '编辑', 'role_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887067807607799810, 1884974935785971713, '删除', 'role_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887067868345516033, 1884974935785971713, '查看', 'role_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887067953414389761, 1884974935785971713, '新增子项', 'role_add_child', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068013644595201, 1884974935785971713, '用户设置', 'role_user', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068081550376962, 1884974935785971713, '权限配置', 'role_auth', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068261720899586, 1879755468701204481, '新增', 'storage_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068312841076737, 1879755468701204481, '编辑', 'storage_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068342335422466, 1879755468701204481, '删除', 'storage_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068379861860354, 1879755468701204481, '查看', 'storage_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068468068073473, 1879755468701204481, '协议配置', 'storage_config', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068574238490625, 1879755772490448898, '删除', 'attach_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068642651783170, 1879755772490448898, '下载', 'attach_download', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068727225729026, 1880186212221272065, '新增', 'params_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068761853902849, 1880186212221272065, '编辑', 'params_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068791499243522, 1880186212221272065, '删除', 'params_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068838941016065, 1880186212221272065, '查看', 'params_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887068927986089986, 1880186212221272065, '新增', 'menu_add', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887068996546183169, 1873719239731916802, '新增', 'menu_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887069027089104898, 1873719239731916802, '编辑', 'menu_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887069058814820354, 1873719239731916802, '删除', 'menu_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887069129673392130, 1873719239731916802, '查看', 'menu_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887069231636922369, 1873719239731916802, '新增子项', 'menu_add_child', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887069393021157377, 1873719239731916802, '设置有效', 'menu_valid', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887069437107486722, 1873719239731916802, '设置无效', 'menu_unvalid', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887799715539025922, 1887852869190205441, '回到首页', 'home', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887853120185745409, 1887852869190205441, '搜索', 'search', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887853247537397761, 1887852869190205441, '全屏', 'fullscreen', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887853394933628930, 1887852869190205441, '个人信息', 'userInfo', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1887853450512351233, 1887852869190205441, '退出', 'logout', 1, 1);
INSERT INTO `sys_menu_button` VALUES (1887854002772164609, 1887852869190205441, '更多', 'more', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1888505303390588929, 1887852869190205441, '提交用户信息', 'submit_user_info', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1888505410617970689, 1887852869190205441, '提交重置密码', 'submit_repassword', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890058395890339842, 1890050634607611905, '新增', 'task_classify_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890058522906447874, 1890050634607611905, '编辑', 'task_classify_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890058711473967105, 1890050634607611905, '删除', 'task_classify_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890058771972608002, 1890050634607611905, '查看', 'task_classify_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890058838888534017, 1890050634607611905, '新增子项', 'task_classify_add_child', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890247984257028098, 1890050839264481281, '新增', 'task_add', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890248018310582273, 1890050839264481281, '删除', 'task_delete', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890248069875355649, 1890050839264481281, '设置启用', 'task_enable', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890248184627318785, 1890050839264481281, '设置停用', 'task_un_enable', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890248241820848130, 1890050839264481281, '查看', 'task_view', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890248280689463298, 1890050839264481281, '编辑', 'task_edit', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890248329901232129, 1890050839264481281, '日志', 'task_log', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890654657647824897, 1890050839264481281, '执行一个', 'task_exec_onece', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890654745023565825, 1890050839264481281, '执行日志', 'task_exec_log', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890654913672335361, 1890050839264481281, '启动任务', 'task_start', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890654970018615298, 1890050839264481281, '停止任务', 'task_stop', 1, 0);
INSERT INTO `sys_menu_button` VALUES (1890655061672546306, 1890050839264481281, 'GlUE编辑器', 'task_glue', 1, 0);

-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization`  (
                                     `id` bigint(0) NOT NULL COMMENT 'ID',
                                     `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                                     `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织名称',
                                     `fullname` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织全称',
                                     `code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织编号',
                                     `type` int(0) NULL DEFAULT 1 COMMENT '组织类型（1单位，0部门）',
                                     `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                                     `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                                     `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_organization
-- ----------------------------
INSERT INTO `sys_organization` VALUES (1850706813621731329, NULL, '寒江科技有限公司', '寒江科技有限公司', 'HJ0001', 1, 1, 1, 0);
INSERT INTO `sys_organization` VALUES (1850706813621731330, 1850706813621731332, '人事部', '人事部', 'HJ001_B001', 0, 1, 3, 0);
INSERT INTO `sys_organization` VALUES (1850706813621731331, 1850706813621731332, '财务部', '财务部', 'HJ001_B002', 0, 0, 4, 0);
INSERT INTO `sys_organization` VALUES (1850706813621731332, 1850706813621731329, '转转子公司', '转转子公司', 'HJ0001C_001', 1, 1, 1, 0);
INSERT INTO `sys_organization` VALUES (1850706813621731333, 1850706813621731329, '合计子公司', '合计子公司', 'HJ001_C002', 1, 1, 2, 0);

-- ----------------------------
-- Table structure for sys_params
-- ----------------------------
DROP TABLE IF EXISTS `sys_params`;
CREATE TABLE `sys_params`  (
                               `id` bigint(0) NOT NULL COMMENT '主键',
                               `param_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
                               `param_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数Key',
                               `param_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数Value',
                               `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否已删除',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_params
-- ----------------------------
INSERT INTO `sys_params` VALUES (1880450866017300482, 'TokenTTL（毫秒）', 'TOKEN_LIVE_TIME', '10800000', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` bigint(0) NOT NULL COMMENT '主键',
                             `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                             `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
                             `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编号',
                             `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                             `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                             `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1111111111111111111, 0, '超级管理员', 'administrator', 1, 0, 0);
INSERT INTO `sys_role` VALUES (1887070057067560961, 0, '管理员', 'admin', 1, 1, 1);
INSERT INTO `sys_role` VALUES (1887430996501467137, 0, '基础角色', 'base_role', 1, 99, 1);

-- ----------------------------
-- Table structure for sys_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_auth`;
CREATE TABLE `sys_role_auth`  (
                                  `role_id` bigint(0) NOT NULL COMMENT '主键',
                                  `auth_id` bigint(0) NOT NULL COMMENT '父ID',
                                  `auth_type` int(0) NOT NULL COMMENT '权限类型（1菜单，2按钮，3接口）'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色_权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_auth
-- ----------------------------

-- ----------------------------
-- Table structure for sys_scheduled
-- ----------------------------
DROP TABLE IF EXISTS `sys_scheduled`;
CREATE TABLE `sys_scheduled`  (
                                  `id` bigint(0) NOT NULL COMMENT 'id',
                                  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
                                  `task_classify` bigint(0) NULL DEFAULT NULL COMMENT '定时任务分类',
                                  `task_describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定时任务描述',
                                  `config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '配置',
                                  `cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron策略',
                                  `path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求路径',
                                  `enable` int(0) NOT NULL DEFAULT 0 COMMENT '是否启用定时任务',
                                  `open_log` int(0) NULL DEFAULT 0 COMMENT '是否开启日志',
                                  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                                  `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                  `create_time` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                  `update_time` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_scheduled
-- ----------------------------
INSERT INTO `sys_scheduled` VALUES (1340008083107086336, '定时任务1', 1890212284891422722, '暂无描述', NULL, '0 0/30 * * * ?', '/sheduled1/execute/do-task1', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-14 17:13:43', '1340460760815370240', '2025-02-15 23:12:29');
INSERT INTO `sys_scheduled` VALUES (1340008083107086338, '定时任务4', 1890212284891422722, '暂无描述', '[]', '0 0/30 * * * ?', '/sheduled1/execute/do-task4', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-14 17:13:43', '1340020279945986048', '2025-02-14 18:02:11');
INSERT INTO `sys_scheduled` VALUES (1340008083111280640, '定时任务2', 1890212284891422722, '暂无描述', '[]', '0 0/30 * * * ?', '/sheduled1/execute/do-task2', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-14 17:13:43', '1340460750656765952', '2025-02-15 23:12:27');
INSERT INTO `sys_scheduled` VALUES (1340008083111280641, '定时任务5', 1890212284891422722, '暂无描述', '[]', '0 0/30 * * * ?', '/sheduled2/execute/do-task1', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-14 17:13:43', '1340020350280269824', '2025-02-14 18:02:27');
INSERT INTO `sys_scheduled` VALUES (1340008083111280642, '定时任务7', 1890212284891422722, '暂无描述', NULL, '0 0/30 * * * ?', '/sheduled2/execute/do-task3', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-14 17:13:43', NULL, NULL);
INSERT INTO `sys_scheduled` VALUES (1340008083111280643, '定时任务8', 1890212284891422722, '暂无描述', NULL, '0 0/30 * * * ?', '/sheduled2/execute/do-task4', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-14 17:13:43', NULL, NULL);
INSERT INTO `sys_scheduled` VALUES (1340008083111280644, '定时任务6', 1890212284891422722, '暂无描述', NULL, '0 0/30 * * * ?', '/sheduled2/execute/do-task2', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-14 17:13:43', NULL, NULL);
INSERT INTO `sys_scheduled` VALUES (1340008083111280645, '定时任务9', 1890212284891422722, '暂无描述', NULL, '0 0/30 * * * ?', '/topic/execute/do-task1', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-14 17:13:43', NULL, NULL);
INSERT INTO `sys_scheduled` VALUES (1340343047564558336, '定时任务3', 1890212284891422722, '暂无描述', NULL, '0 0/30 * * * ?', '/sheduled1/execute/do-task3', 0, 1, 'ANNOTATION', '1111111111111111111', '2025-02-15 15:24:44', NULL, NULL);
INSERT INTO `sys_scheduled` VALUES (1890305589801127937, 'Demo模版', 1890212284891422722, NULL, '[{\"key\":\"id\",\"value\":\"123456789098765432\"}]', '0 0 0/1 * * ?', 'com.tiger.job.task.scheduled.Demo', 0, 1, 'TEMPLATE', '1339985116268593152', '2025-02-14 15:42:27', '1340621657760333824', '2025-02-16 09:51:50');
INSERT INTO `sys_scheduled` VALUES (1890663872508481538, 'glue测试', 1890212284891422722, NULL, 'import com.tiger.job.common.JobHandler;\nimport com.tiger.job.task.topic.ComponentBean;\nimport org.springframework.beans.factory.annotation.Autowired;\n\npublic class Demo extends JobHandler {\n    @Autowired\n    private ComponentBean componentBean;\n  \n    @Override\n    public void execute() throws Exception {\n        System.out.println(\"do something...\");\n        componentBean.invoke();\n    }\n}', '33 10/15 * * * ?', '', 0, 1, 'GLUE', '1340343399005290496', '2025-02-15 15:26:08', '1340460784580296704', '2025-02-15 23:12:35');
INSERT INTO `sys_scheduled` VALUES (1891035278350082049, 'glue测试2', 1890212284891422722, NULL, 'import com.tiger.job.common.JobHandler;\n\npublic class Demo extends JobHandler {\n\n    @Override\n    public void execute() throws Exception {\n        throw new RuntimeException(\"错误日志查看测试\");\n    }\n}', '57 0/15 * * * ?', '', 0, 1, 'GLUE', '1340714804834205696', '2025-02-16 16:01:58', '1340716258546417664', '2025-02-16 16:07:45');

-- ----------------------------
-- Table structure for sys_scheduled_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_scheduled_log`;
CREATE TABLE `sys_scheduled_log`  (
                                      `id` bigint(0) NOT NULL COMMENT 'id',
                                      `task_id` bigint(0) NULL DEFAULT NULL COMMENT '定时任务ID',
                                      `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
                                      `execute_status` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行状态',
                                      `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
                                      `execute_time` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_scheduled_log
-- ----------------------------
INSERT INTO `sys_scheduled_log` VALUES (1340084657881874432, 1890305589801127937, 'Demo模版', 'success', NULL, '2025-02-14 22:18:00');
INSERT INTO `sys_scheduled_log` VALUES (1340084699845885952, 1890305589801127937, 'Demo模版', 'success', NULL, '2025-02-14 22:18:10');
INSERT INTO `sys_scheduled_log` VALUES (1340084741780537344, 1890305589801127937, 'Demo模版', 'success', NULL, '2025-02-14 22:18:20');
INSERT INTO `sys_scheduled_log` VALUES (1340084783702605824, 1890305589801127937, 'Demo模版', 'success', NULL, '2025-02-14 22:18:30');
INSERT INTO `sys_scheduled_log` VALUES (1340458187412733952, 1890663872508481538, 'glue测试', 'success', NULL, '2025-02-15 23:02:16');
INSERT INTO `sys_scheduled_log` VALUES (1340458859579310080, 1890663872508481538, 'glue测试', 'success', NULL, '2025-02-15 23:04:56');
INSERT INTO `sys_scheduled_log` VALUES (1340716596905115648, 1891035278350082049, 'glue测试2', 'fail', 'java.lang.RuntimeException: 错误日志查看测试\r\n	at org.codehaus.groovy.vmplugin.v8.IndyInterface.fromCache(IndyInterface.java:321)\r\n	at Demo.execute(Script_1053e90193ec87306820aaf9b38e57f0.groovy:7)\r\n	at com.tiger.job.core.doJob.invoker.GlueInvoker.invoke(GlueInvoker.java:34)\r\n	at com.tiger.job.core.doJob.JobInvoke.execute(JobInvoke.java:43)\r\n	at com.tiger.job.core.executor.impl.ManualExecutor.execute(ManualExecutor.java:33)\r\n	at com.tiger.job.core.controller.TaskExecutorController.execute(TaskExecutorController.java:35)\r\n	at com.tiger.job.core.controller.TaskExecutorController$$FastClassBySpringCGLIB$$517cac04.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:793)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor.invoke(MethodSecurityInterceptor.java:61)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:708)\r\n	at com.tiger.job.core.controller.TaskExecutorController$$EnhancerBySpringCGLIB$$47b72d20.execute(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1067)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\r\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:111)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:327)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:115)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:81)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:122)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:116)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:126)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:81)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:149)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at com.tiger.job.security.JwtAuthenticationTokenFilter.doFilterInternal(JwtAuthenticationTokenFilter.java:30)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:103)\r\n	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:89)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:112)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:82)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:55)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:211)\r\n	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:183)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:354)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:267)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:96)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:360)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:399)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\r\n	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:890)\r\n	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1787)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\r\n	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\r\n	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:750)\r\n', '2025-02-16 16:09:06');

-- ----------------------------
-- Table structure for sys_scheduled_log20230630153000
-- ----------------------------
DROP TABLE IF EXISTS `sys_scheduled_log20230630153000`;
CREATE TABLE `sys_scheduled_log20230630153000`  (
                                                    `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
                                                    `task_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定时任务ID',
                                                    `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
                                                    `execute_status` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行状态',
                                                    `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
                                                    `execute_time` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_scheduled_log20230630153000
-- ----------------------------
INSERT INTO `sys_scheduled_log20230630153000` VALUES ('1', '1', '1', '1', '1', '2023-02-12 12:12:12');
INSERT INTO `sys_scheduled_log20230630153000` VALUES ('1124354154928537600', '1117158599655686149', '定时任务9', 'success', NULL, '2023-06-30 15:02:00');
INSERT INTO `sys_scheduled_log20230630153000` VALUES ('1124354406498697216', '1117158599655686149', '定时任务9', 'success', NULL, '2023-06-30 15:03:00');
INSERT INTO `sys_scheduled_log20230630153000` VALUES ('2', '1', '1', '1', '1', '2023-03-12 12:12:12');
INSERT INTO `sys_scheduled_log20230630153000` VALUES ('3', '1', '1', '1', '1', '2023-04-12 12:12:12');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` bigint(0) NOT NULL COMMENT '主键',
                             `code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编号',
                             `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
                             `nickname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
                             `phone` bigint(0) NULL DEFAULT NULL COMMENT '手机号码',
                             `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱号码',
                             `org_id` bigint(0) NULL DEFAULT NULL COMMENT '所属组织',
                             `account` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
                             `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
                             `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效',
                             `sort` int(0) NULL DEFAULT NULL COMMENT '用户排序',
                             `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1111111111111111111, 'administrator', '超级管理员', '超级管理员', 17865454234, 'administrator@hj.com', 1850706813621731329, 'administrator', 'c0dcc99913baee40588da3bf134a2205', 1, 1, 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                  `role_id` bigint(0) NOT NULL COMMENT '角色ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户_角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1111111111111111111, 1111111111111111111);
INSERT INTO `sys_user_role` VALUES (1873746662934450177, 1887430996501467137);
INSERT INTO `sys_user_role` VALUES (1873746662934450177, 1887070057067560961);
INSERT INTO `sys_user_role` VALUES (1873749058125398017, 1887430996501467137);

SET FOREIGN_KEY_CHECKS = 1;
