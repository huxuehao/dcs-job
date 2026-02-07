package com.hxh.job.params.controller;

import com.hxh.job.common.annotation.MenuTag;
import com.hxh.job.common.entity.Params;
import com.hxh.job.common.mp.support.MP;
import com.hxh.job.common.mp.support.PageParams;
import com.hxh.job.common.r.R;
import com.hxh.job.params.service.TParamsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：系统参数
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/params")
@MenuTag(code = "system_params")
public class TParamsController {
    private final TParamsService paramsService;

    public TParamsController(TParamsService paramsService) {
        this.paramsService = paramsService;
    }

    @PreAuthorize("@ps.hasPermission('get::params:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> page(Params params, PageParams pageParams) {
        return R.data(paramsService.page(MP.getPage(pageParams), MP.getQueryWrapper(params)));
    }

    @PreAuthorize("@ps.hasPermission('post::params:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody Params params) {
        return R.data(paramsService.saveV2(params));
    }

    @PreAuthorize("@ps.hasPermission('post::params:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> update(@RequestBody Params params) {
        return R.data(paramsService.updateByIdV2(params));
    }

    @PreAuthorize("@ps.hasPermission('post::params:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(paramsService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::params:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(paramsService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('get::params:fetch-value-by-key')")
    @GetMapping(value = "/fetch-value-by-key", name = "根据key获取value")
    public R<?> fetchValueByKey(@RequestParam String key) {
        return R.data(paramsService.fetchValueByKey(key));
    }

}
