package com.tiger.job.core.beanScan;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;
import com.tiger.job.common.exception.member.EmptyTaskPathException;
import com.tiger.job.common.exception.member.SameTaskPathException;
import com.tiger.job.common.util.BeanUtil;
import com.tiger.job.common.util.ClassUtil;
import com.tiger.job.common.constant.ScanProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 描述：扫描器，扫描定时任务
 *
 * @author huxuehao
 **/
@Component
public class SchedulerScan {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ScanProperties scanProperties;
    private final AutoInsert autoInsert;

    public SchedulerScan(ScanProperties scanProperties, AutoInsert autoInsert) {
        this.scanProperties = scanProperties;
        this.autoInsert = autoInsert;
    }

    /**
     * 扫描定时任务方法
     * @return Map<String, Map<Object, Method>>
     *             String: TaskAPI注解的path属性
     *                     Map<Object, Method>:key是含有SchedulerBean注解的类实体
     *                                         value是含有TaskAPI注解的方法
     */
    public Map<String, Map<Object, Method>> schedulerScanMethod() throws IOException, ClassNotFoundException {
        List<Class<?>> schedulerBeanClass = getSchedulerBeanClass(ClassUtil.getClasses(scanProperties.getTaskPackage()));
        return getTaskPathMethod(schedulerBeanClass);
    }

    /**
     * 获取带有SchedulerBean注解的Class
     * @param classes 包路径下扫描到的Class集合
     * @return 带有SchedulerBean注解的Class
     */
    private List<Class<?>> getSchedulerBeanClass(List<Class<?>> classes) {
        List<Class<?>> schedulerBeanClass = new ArrayList<>();
        for (Class<?> aClass : classes) {
            if (aClass.isAnnotationPresent(SchedulerBean.class)) {
                schedulerBeanClass.add(aClass);
            }
        }
        return schedulerBeanClass;
    }

    /**
     * 获取带有TaskAPI注解的方法信息
     * @param classes 带有SchedulerBean注解的Class
     * @return Map<String, Map<Object, Method>>
     *             String:TaskAPI注解的path属性
     *                     Map<Object, Method>:key是含有SchedulerBean注解的类实体
     *                                         value是含有TaskAPI注解的方法
     */
    private Map<String, Map<Object, Method>> getTaskPathMethod(List<Class<?>> classes) {
        Map<String, Map<Object, Method>> methodMap = new HashMap<>();
        List<TaskPath> taskPaths = new ArrayList<>();
        if (classes == null || classes.size() == 0) {
            return methodMap;
        }
        /* 遍历Class，获取合法的方法 */
        for (Class<?> aClass : classes) {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (!method.isAnnotationPresent(TaskPath.class)) {
                    continue;
                }

                TaskPath annotation = method.getAnnotation(TaskPath.class);
                String path = annotation.path();
                if (path == null || "".equals(path)) {
                    throw new EmptyTaskPathException();
                }
                method.setAccessible(true); /* 去除私有方法的限制 */
                Map<Object, Method> mapValue = new HashMap<Object, Method>() {{
                    try {
                        /* 先从bean中获取, 若取不到则手动创建实例 */
                        put(BeanUtil.getBean(aClass), method);
                    } catch (Exception e) {
                        try {
                            /* 手动创建实例 */
                            put(aClass.newInstance(), method);
                        } catch (InstantiationException ex) {
                            throw new RuntimeException("实例化异常，请确保[" + aClass.getName() +"]具默认构造函数;" + ex.getMessage());
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException("非法访问异常，请确保[" + aClass.getName() +"]的默认构造函为public;" + ex.getMessage());
                        }
                    }
                }};
                /* 判断是否有重复的path */
                if (methodMap.containsKey(path)) {
                    throw new SameTaskPathException("存在相同的定时任务path：" + path + "，请确保path的唯一性");
                }
                taskPaths.add(annotation);
                methodMap.put(path, mapValue);
                log.info("定时任务：已经完成对 [ {}({}) ] 定时任务的扫描", annotation.name(),annotation.path());
            }
        }
        if (!taskPaths.isEmpty()) {
            autoInsert.invoke(taskPaths);
        }
        return methodMap;
    }
}
