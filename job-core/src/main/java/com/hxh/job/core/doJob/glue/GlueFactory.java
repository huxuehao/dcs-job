package com.hxh.job.core.doJob.glue;

import com.hxh.job.common.JobHandler;
import com.hxh.job.common.util.BeanUtil;
import com.hxh.job.common.util.Func;
import groovy.lang.GroovyClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 描述：Glue 工厂
 *
 * @author huxuehao
 **/
public class GlueFactory {
    private static final GlueFactory glueFactory = new GlueFactory();
    private final GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
    private final ConcurrentMap<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

    public static GlueFactory getFactory() {
        return glueFactory;
    }


    public JobHandler loadNewInstance(String codeSource) throws Exception {
        if (Func.isNotEmpty(codeSource)) {
            // 基于源码获取Class
            Class<?> clazz = getCodeSourceClass(codeSource);
            if (clazz == null) {
                throw new IllegalArgumentException("loadNewInstance 执行失败, Glue 脚本为空");
            }

            // 实例化对象
            Object instance = clazz.newInstance();
            if (!(instance instanceof JobHandler)) {
                throw new IllegalArgumentException("loadNewInstance 执行失败, Glue 脚本类需要继承 " + JobHandler.class.getName());
            }

            //依赖注入
            dependencyInjection(instance);

            return (JobHandler) instance;
        }

        throw new IllegalArgumentException("loadNewInstance 执行失败, Glue 脚本为空");
    }

    private Class<?> getCodeSourceClass(String codeSource) {
        try {
            // 对codeSource进行md5加密，获取到的字符串作为缓存的key使用
            byte[] md5 = MessageDigest.getInstance("MD5").digest(codeSource.getBytes());
            String md5Str = new BigInteger(1, md5).toString(16);

            Class<?> clazz = CLASS_CACHE.get(md5Str);
            if (clazz == null) {
                clazz = groovyClassLoader.parseClass(codeSource);
                CLASS_CACHE.putIfAbsent(md5Str, clazz);
            }
            return clazz;
        } catch (Exception e) {
            return groovyClassLoader.parseClass(codeSource);
        }
    }

    /**
     * 依赖注入，从spring容器中获取bean，进行依赖注入
     *
     * @param instance Glue对象实例
     */
    private void dependencyInjection(Object instance) {
        if (instance == null) {
            return;
        }

        if (!BeanUtil.checkBeanFactory()) {
            return;
        }

        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 排除静态字段
            if (Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }

            /*
             * 判断当前filed是否需要注入依赖，并重spring中获取依赖
             */

            Object beanObj = null;
            Resource resource;
            // 成立条件：判断当前Field是否被 @Resource 标记
            if ((resource = AnnotationUtils.getAnnotation(declaredField, Resource.class)) != null) {
                try {
                    if (Func.isNotEmpty(resource.name())) {
                        beanObj = BeanUtil.getBean(resource.name());
                    } else {
                        beanObj = BeanUtil.getBean(declaredField.getName());
                    }
                } catch (Exception ignored) {
                }
                if (beanObj != null) {
                    beanObj = BeanUtil.getBean(declaredField.getType());
                }
            }
            // 成立条件：判断当前Field是否被 @Autowired 标记
            else if (AnnotationUtils.getAnnotation(declaredField, Autowired.class) != null) {
                Qualifier qualifier = AnnotationUtils.getAnnotation(declaredField, Qualifier.class);
                if (qualifier != null && Func.isNotEmpty(qualifier.value())) {
                    beanObj = BeanUtil.getBean(qualifier.value());
                } else {
                    beanObj = BeanUtil.getBean(declaredField.getType());
                }
            }

            if (beanObj != null) {
                declaredField.setAccessible(true);
                try {
                    declaredField.set(instance, beanObj);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }
}
