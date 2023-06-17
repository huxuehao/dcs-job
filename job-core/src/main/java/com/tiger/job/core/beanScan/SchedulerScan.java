package com.tiger.job.core.beanScan;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;
import com.tiger.job.common.exception.member.EmptyTaskPathException;
import com.tiger.job.common.exception.member.SameTaskPathException;
import com.tiger.job.common.util.ClassUtil;
import com.tiger.job.common.constant.ScanProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName SchedulerScan
 * @Description 扫描器，扫描定时任务
 * @Author huxuehao
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
        List<Class> schedulerBeanClass = getSchedulerBeanClass(ClassUtil.getClasses(scanProperties.getTaskPackage()));
        return getTaskPathMethod(schedulerBeanClass);
    }

    /**
     * 获取带有SchedulerBean注解的Class
     * @param classes 包路径下扫描到的Class集合
     * @return 带有SchedulerBean注解的Class
     */
    private List<Class> getSchedulerBeanClass(List<Class> classes) {
        List<Class> schedulerBeanClass = new ArrayList<>();
        for (Class aClass : classes) {
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
    private Map<String, Map<Object, Method>> getTaskPathMethod(List<Class> classes) {
        Map<String, Map<Object, Method>> methodMap = new HashMap<>();
        List<TaskPath> taskPaths = new ArrayList<>();
        if (classes == null || classes.size() == 0) {
            return methodMap;
        }
        /* 遍历Class，获取合法的方法 */
        for (Class aClass : classes) {
            List<Method> declaredMethods = Arrays.asList(aClass.getDeclaredMethods());
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(TaskPath.class)) {
                    TaskPath annotation = method.getAnnotation(TaskPath.class);
                    String path = annotation.path();
                    if (path == null || "".equals(path)) {
                        throw new EmptyTaskPathException();
                    }
                    method.setAccessible(true); /* 去除私有方法的限制 */
                    Map<Object, Method> mapValue = new HashMap<Object, Method>() {{
                        try {
                            put(aClass.newInstance(), method);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }};
                    /* 判断是否有重复的path */
                    if (methodMap.containsKey(path)) {
                        throw new SameTaskPathException("存在相同的定时任务path：" + path + "，请确保path的唯一性");
                    }
                    taskPaths.add(annotation);
                    methodMap.put(path, mapValue);
                    log.info("定时任务：已经完成对 [ {} ] 定时任务的扫描", path);
                }
            }
        }
        autoInsert.invoke(taskPaths);
        return methodMap;
    }
}
