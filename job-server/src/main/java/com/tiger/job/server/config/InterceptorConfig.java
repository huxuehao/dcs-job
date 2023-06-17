package com.tiger.job.server.config;

import com.tiger.job.server.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @ClassName AuthInterceptConfig
 * @Description 拦截器配置
 * @Author StudiousTiger
 **/
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /* 拦截所有的请求，通过请求映射到的方法上的注解进行判断是否需要权限验证 */
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /* 添加映射路径 */
        registry.addMapping("/**")
                /* 设置放行哪些原始域 SpringBoot2.4.4下低版本使用.allowedOrigins("*") */
                .allowedOrigins("*")
                /* 放行哪些请求方式 */
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // .allowedMethods("*") //或者放行全部
                /* 放行哪些原始请求头部信息 */
                .allowedHeaders("*");
    }
}
