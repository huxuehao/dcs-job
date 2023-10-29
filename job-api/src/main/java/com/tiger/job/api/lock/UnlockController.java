package com.tiger.job.api.lock;

import com.tiger.job.common.annotation.LoginAuth;
import com.tiger.job.common.response.Response;
import com.tiger.job.core.unlock.Unlock;
import org.springframework.context.annotation.Description;
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
public class UnlockController {
    private final Unlock unlock;

    public UnlockController(Unlock unlock) {
        this.unlock = unlock;
    }

    @LoginAuth
    @GetMapping("/task/lock/by-id")
    @Description("根据定时任务id解锁，定时任务锁")
    public Response unlockByTaskId(@RequestParam("id") String id) {
        unlock.unlockTask(Collections.singletonList(id));
        return Response.success("解锁成功");
    }

    @LoginAuth
    @GetMapping("/task/lock/all")
    @Description("解锁全部的定时任务锁")
    public Response unlockTaskAll() {
        unlock.unlockTaskAll();
        return Response.success("解锁成功");
    }

    @LoginAuth
    @GetMapping("/task/lock/rotate")
    @Description("解锁日志轮转定时任务锁")
    public Response unLockRotate() {
        unlock.unLockRotate();
        return Response.success("解锁成功");
    }
}
