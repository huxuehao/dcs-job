package com.tiger.job.api.login;

import com.tiger.job.common.annotation.LoginAuth;
import com.tiger.job.common.annotation.PassAuth;
import com.tiger.job.common.entity.LoginBody;
import com.tiger.job.common.response.Response;
import com.tiger.job.server.service.UserService;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：登录
 *
 * @author huxuehao
 */
@RestController
public class SysLoginController {

    private final UserService userService;

    public SysLoginController(UserService userService) {
        this.userService = userService;
    }

    @PassAuth
    @Description(value = "登录")
    @PostMapping(value = "/login")
    public Response login(@RequestBody LoginBody loginBody) {
        return Response.success(userService.validateLogin(loginBody));
    }

    @LoginAuth
    @Description(value = "退出登录")
    @GetMapping(value = "/logout")
    public Response logout(@RequestParam("userId") String userId) {
        userService.logout(userId);
        return Response.success("退出成功");
    }
}
