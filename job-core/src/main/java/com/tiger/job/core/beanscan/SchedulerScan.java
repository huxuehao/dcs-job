package com.tiger.job.core.beanscan;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;
import com.tiger.job.common.exception.member.EmptyTaskPathException;
import com.tiger.job.common.exception.member.NotFolderException;
import com.tiger.job.common.exception.member.SameTaskPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @ClassName SchedulerScan
 * @Description TODO
 * @Author huxuehao
 **/
@Component
public class SchedulerScan {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${tiger.scheduled-task.task-package}")
    String taskPackage;

    /**
     * 扫描定时任务方法
     * @return Map<String, Map<Object, Method>>
     *             String: TaskAPI注解的path属性
     *                     Map<Object, Method>:key是含有SchedulerBean注解的类实体
     *                                         value是含有TaskAPI注解的方法
     */
    public Map<String, Map<Object, Method>> schedulerScanMethod() {
        String dirPath = getDirPath(taskPackage);
        List<Class<?>> classes = scanClassForDir(taskPackage,dirPath);
        List<Class<?>> schedulerBeanClass = getSchedulerBeanClass(classes);
        return getTaskAPIMethod(schedulerBeanClass);
    }

    /**
     * 根据包路径获取文件夹路径
     * @param packagePath 包路径
     * @return
     */
    private String getDirPath(String packagePath) {
        /* 将包名替换成相对路径 */
        String path = packagePath.replace(".", File.separator);
        Enumeration<URL> enums = null;
        try {
            /* 类加载器加载资源 */
            enums = ClassLoader.getSystemResources(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dirPath = "";
        assert enums != null;
        while (enums.hasMoreElements()) {
            URL url = enums.nextElement();
            if (url != null && "file".equals(url.getProtocol())) {
                try {
                    /* 获取包路径对应的文件全路径 */
                    dirPath = URLDecoder.decode(url.getPath(), String.valueOf(StandardCharsets.UTF_8));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return dirPath;
    }

    /**
     * 获取包路径对应的文件夹下了所有的Class
     * @param packagePath 包路径
     * @param dirPath 包路径对应的文件夹路径
     * @return 包路径下所有的Class
     */
    private List<Class<?>>  scanClassForDir(String packagePath, String dirPath) {
        List<Class<?>> classList = new ArrayList<>();
        File dir = new File(dirPath);
        if (dir.listFiles() == null || Objects.requireNonNull(dir.listFiles()).length == 0) {
            return classList;
        }
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                /* 当时文件夹时，递归*/
                if (file.isDirectory()) {
                    List<Class<?>> list = scanClassForDir(packagePath, file.getPath());
                    classList.addAll(list);
                } else {
                    /* 根据包路径获取相对路径 */
                    String relativePath = packagePath.replace(".", File.separator);
                    /* 获取文件全路径 */
                    String fullClassName = file.getPath();
                    /* 获取文件的相对路径 */
                    fullClassName = fullClassName.substring(fullClassName.lastIndexOf(relativePath));
                    /* 将文件的相对路径转换成包路径 */
                    fullClassName = fullClassName.replace(File.separator, ".");
                    /* 去除最后的.class，得到类的包路径 */
                    fullClassName = fullClassName.substring(0, fullClassName.lastIndexOf("."));
                    try {
                        /* 根据类的包路径获取Class*/
                        classList.add(Class.forName(fullClassName));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return classList;
        } else {
            throw new NotFolderException("[" + dirPath + "]不是文件夹");
        }
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
    private Map<String, Map<Object, Method>> getTaskAPIMethod(List<Class<?>> classes) {
        Map<String, Map<Object, Method>> methodMap = new HashMap<>();
        if (classes == null || classes.size() == 0) {
            return methodMap;
        }
        /* 遍历Class，获取合法的方法 */
        for (Class<?> aClass : classes) {
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
                    methodMap.put(path, mapValue);
                    log.info("定时任务：已经完成对 [ {} ] 定时任务的扫描", path);
                }
            }
        }
        return methodMap;
    }
}
