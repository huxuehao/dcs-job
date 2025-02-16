package com.tiger.job.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tiger.job.common.annotation.MenuTag;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.entity.SysGlueVersion;
import com.tiger.job.common.mp.support.MP;
import com.tiger.job.common.r.R;
import com.tiger.job.common.util.AuthUtil;
import com.tiger.job.common.util.Func;
import com.tiger.job.server.service.GlueVersionService;
import com.tiger.job.server.service.ScheduleTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * 描述：glue版本控制
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping(value = "/job/glue")
@MenuTag(code = "job_task")
public class GlueVersionController {
    private final GlueVersionService glueVersionService;
    private final ScheduleTaskService scheduleTaskService;

    public GlueVersionController(GlueVersionService glueVersionService, ScheduleTaskService scheduleTaskService) {
        this.glueVersionService = glueVersionService;
        this.scheduleTaskService = scheduleTaskService;
    }

    @PreAuthorize("@ps.hasPermission('get::job:glue:list-by-task-id')")
    @GetMapping(value = "/list-by-task-id", name = "Glue列表")
    public R<?> listByTaskId(@RequestParam Long taskId) {
        SysGlueVersion glueVersion = new SysGlueVersion();
        glueVersion.setTaskId(taskId);

        QueryWrapper<SysGlueVersion> qw = MP.getQueryWrapper(glueVersion);
        qw.last("ORDER BY create_time desc");
        qw.select("task_id","remark","type","create_user", "create_time");

        return R.data(glueVersionService.list(qw));
    }

    @PreAuthorize("@ps.hasPermission('get::job:glue:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据标记唯一获取")
    public R<?> listByTaskId(@RequestParam Long taskId, @RequestParam String createTime) {
        SysGlueVersion glueVersion = new SysGlueVersion();
        glueVersion.setTaskId(taskId);
        glueVersion.setCreateTime(createTime);

        return R.data(glueVersionService.getOne(MP.getQueryWrapper(glueVersion)));
    }

    @PreAuthorize("@ps.hasPermission('post::job:glue:save')")
    @PostMapping(value = "/save", name = "Glue保存")
    public R<?> save(@RequestBody SysGlueVersion glueVersion) {
        if (Func.isEmpty(glueVersion.getConfig())) {
            throw new RuntimeException("脚本内容不可为空");
        }
        glueVersion.setType("JAVA");
        glueVersion.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        glueVersion.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return R.data(glueVersionService.save(glueVersion));
    }

    @Transactional
    @PreAuthorize("@ps.hasPermission('post::job:glue:save-publish')")
    @PostMapping(value = "/save-publish", name = "Glue保存并发布")
    public R<?> savePublish(@RequestBody SysGlueVersion glueVersion) {
        if (Func.isEmpty(glueVersion.getConfig())) {
            throw new RuntimeException("脚本内容不可为空");
        }

        glueVersion.setType("JAVA");
        glueVersion.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        glueVersion.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        glueVersionService.save(glueVersion);

        UpdateWrapper<ScheduledConfigEntity> uw = new UpdateWrapper<>();
        uw.set("config", glueVersion.getConfig());
        uw.eq("id", glueVersion.getTaskId());
        scheduleTaskService.update(uw);

        ScheduledConfigEntity scheduleTask = scheduleTaskService.getById(glueVersion.getTaskId());
        if (scheduleTask.getEnable() == 1) {
            scheduleTaskService.openSchedule(Collections.singletonList(scheduleTask.getId()));
        }

        return R.data(true);
    }
}
