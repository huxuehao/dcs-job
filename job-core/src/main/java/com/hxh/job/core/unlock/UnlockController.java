package com.hxh.job.core.unlock;

import com.hxh.job.common.annotation.MenuTag;
import com.hxh.job.common.r.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 描述：解锁分布式锁
 *
 * @author huxuehao
 */
@RestController
@RequestMapping(value = "/unlock")
@MenuTag(code = "home")
public class UnlockController {
    private final Unlock unlock;

    public UnlockController(Unlock unlock) {
        this.unlock = unlock;
    }

    @PreAuthorize("@ps.hasPermission('get::unlock:task-lock-by-id')")
    @GetMapping(value = "/task/lock/by-id", name = "根据定时任务id解锁，定时任务锁")
    public R<?> unlockByTaskId(@RequestParam("id") String id) {
        unlock.unlockTask(Collections.singletonList(id));
        return R.success("解锁成功");
    }

    @PreAuthorize("@ps.hasPermission('get::unlock:task-lock-all')")
    @GetMapping(value = "/task/lock/all", name = "解锁全部的定时任务锁")
    public R<?> unlockTaskAll() {
        unlock.unlockTaskAll();
        return R.success("解锁成功");
    }

    @PreAuthorize("@ps.hasPermission('get::unlock:task-lock-rotate')")
    @GetMapping(value = "/task/lock/rotate", name = "解锁日志轮转定时任务锁")
    public R<?> unLockRotate() {
        unlock.unLockRotate();
        return R.success("解锁成功");
    }
}
