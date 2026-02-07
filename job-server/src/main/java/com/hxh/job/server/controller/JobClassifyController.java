package com.hxh.job.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hxh.job.common.annotation.MenuTag;
import com.hxh.job.common.entity.Classify;
import com.hxh.job.common.r.R;
import com.hxh.job.server.service.ClassifyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：任务分类
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping(value = "/job/classify")
@MenuTag(code = "job_classify")
public class JobClassifyController {
    private final ClassifyService classifyService;

    public JobClassifyController(ClassifyService classifyService) {
        this.classifyService = classifyService;
    }

    @PreAuthorize("@ps.hasPermission('post::job:classify:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody Classify body) {
        QueryWrapper<Classify> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        List<Classify> list = classifyService.list(qw);
        if (list == null || list.isEmpty()) {
            return R.data(classifyService.save(body));
        } else {
            return R.fail("分类编号已存在");
        }
    }

    @PreAuthorize("@ps.hasPermission('post::job:classify:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(classifyService.deleteAllById(ids));
    }

    @PreAuthorize("@ps.hasPermission('post::job:classify:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> update(@RequestBody Classify body) {
        QueryWrapper<Classify> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        qw.ne("id", body.getId());
        List<Classify> list = classifyService.list(qw);
        if (list == null || list.isEmpty()) {
            if (body.getParentId() == null) {
                body.setParentId(0L);
            }
            return R.data(classifyService.updateById(body));
        } else {
            return R.fail("分类编号已存在");
        }
    }

    @PreAuthorize("@ps.hasPermission('get::job:classify:tree')")
    @GetMapping(value = "/tree", name = "树形列表")
    public R<?> tree(Classify classify) {
        return R.data(classifyService.tree(classify));
    }

    @PreAuthorize("@ps.hasPermission('get::job:classify:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(classifyService.getById(id));
    }
}
