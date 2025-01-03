package com.tiger.job.core.doJob;

import com.tiger.job.common.ScheduledTaskTemplate;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.exception.member.NotMatchPathException;
import com.tiger.job.common.util.BeanUtil;
import com.tiger.job.common.util.MeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：基于模版模式去执行
 *
 * @author huxuehao
 **/
@Component
public class InvokeTemplate extends AbstractInvoke {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String invoke(ScheduledConfigEntity task) {
        String message = null;
        /* 根据类路径创建执行对象map */
        Map<Object, Method> objectMethodMap = createObjectMethodMap(task.getPath());
        if (objectMethodMap == null) {
            return "path[" + task.getPath() + "]未匹配到对应的定时任务";
        }

        /* objectMethodMap 中的Key是已经实例化过的对象，value是定时任务的方法载体 */
        for (Map.Entry<Object, Method> entry : objectMethodMap.entrySet()) {
            /* 准备参数*/
            Object[] params = new Object[]{task};
            /* 执行定时任务*/
            try {
                entry.getValue().invoke(entry.getKey(), params);
                log.info("定时任务[{}]执行成功", task.getName());
            } catch (Exception e) {
                log.error("定时任务[{}]执行失败", task.getName());
                message = MeUtil.catchExceptionStackInfo(e);
            }
            break;
        }

        return message;
    }

    /**
     * 根据类路径创建执行对象map
     * @param classPath 执行类路径
     */
    private Map<Object, Method> createObjectMethodMap(String classPath) {
        Class<?> clazz;
        try {
            // 获取Class
            clazz = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            throw new NotMatchPathException("classPath[" + classPath + "]未匹配到对应的执行类");
        }

        // 类型匹配
        if (!ScheduledTaskTemplate.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("packagePath不是ScheduledTaskTemplate的实例");
        }

        // 获取所有声明的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 检查方法名称是否为doTask
            if(!"doTask".equals(method.getName())) {
                return null;
            }
            // 检查参数数量是否为1
            if (method.getParameterCount() != 1) {
                return null;
            }
            // 获取第一个参数的类型，并检查参数类型是否为ScheduleTaskDto
            Class<?> paramType = method.getParameterTypes()[0];
            if (!paramType.equals(ScheduledConfigEntity.class)) {
                return null;
            }

            method.setAccessible(true);
            return new HashMap<Object, Method>() {{
                try {
                    /* 先从bean中获取, 若取不到则手动创建实例 */
                    put(BeanUtil.getBean(clazz), method);
                } catch (Exception e) {
                    try {
                        /* 手动创建实例 */
                        put(clazz.newInstance(), method);
                    } catch (InstantiationException ex) {
                        throw new RuntimeException("实例化异常，请确保[" + clazz.getName() +"]具默认构造函数;" + ex.getMessage());
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("非法访问异常，请确保[" + clazz.getName() +"]的默认构造函为public;" + ex.getMessage());
                    }
                }
            }};
        }
        return null;
    }
}
