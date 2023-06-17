package com.tiger.job.server.interceptor;

import com.tiger.job.common.annotation.LoginAuth;
import com.tiger.job.common.annotation.PassAuth;
import com.tiger.job.common.entity.SysUser;
import com.tiger.job.common.exception.member.ErrorException;
import com.tiger.job.common.exception.member.InterfaceNotAuthException;
import com.tiger.job.common.exception.member.NotAuthException;
import com.tiger.job.common.exception.member.NotFoundException;
import com.tiger.job.server.service.UserService;
import com.tiger.job.common.util.BeanUtil;
import com.tiger.job.common.util.CacheUtil;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.common.util.TokenUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @ClassName AuthIntercept
 * @Description 权限认证的切面
 * @Author StudiousTiger
 **/
@Component
public class AuthInterceptor implements HandlerInterceptor {

    CacheUtil cacheUtil = BeanUtil.getBean(CacheUtil.class);

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        /* 如果不是映射到方法中，那么直接通过 */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        /* 获取请求头中的 token */
        String token = request.getHeader("OptSql-Auth");
        /* 获取映射的方法 */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        /* 判断是否能够找到对应的请求*/
        if("org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController".equals(method.getDeclaringClass().getName())) {
            throw new NotFoundException("请求找不到对应的接口");
        }
        /* 判断是否跳过认证 */
        else if (method.isAnnotationPresent(PassAuth.class)) {
            PassAuth annotation = method.getAnnotation(PassAuth.class);
            if (annotation.required()) {
                return true;
            } else {
                throw new ErrorException("访问授权失败");
            }
        }
        /* 判断是否需要登录认证 */
        else if (method.isAnnotationPresent(LoginAuth.class)) {
            LoginAuth annotation = method.getAnnotation(LoginAuth.class);
            if (annotation.required()) {
                /* token 不存在*/
                if (MeUtil.isEmpty(token)) {
                    throw new NotAuthException("访问授权失败，请重新登录");
                }
                /* 从token中获取用户ID */
                String userId = TokenUtil.parseToken(token).getSubject();
                Object cacheToken;
                try {
                    /* 从缓存中获取token */
                    cacheToken = cacheUtil.get(userId);
                    if (MeUtil.isEmpty(cacheToken) || !token.equals(cacheToken.toString())) {
                        throw new NotAuthException("访问授权失败，请重新登录");
                    }
                    /*cacheUtil.set(userId, token, Constant.REF_LIVE_TIME);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    /* 查询数据库 */
                    SysUser sysUser = BeanUtil.getBean(UserService.class).selectUserById(userId);
                    if (MeUtil.isEmpty(sysUser)) {
                        throw new NotAuthException("访问授权失败，请重新登录");
                    }
                    /*cacheUtil.set(userId, token, Constant.LIVE_TIME);*/
                }
                return true;
            } else {
                throw new ErrorException("访问授权失败");
            }
        }
        /* 判断接口是否具有权限注解 */
        else {
            throw new InterfaceNotAuthException("接口没有进行权限标记");
        }
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {

    }
}
