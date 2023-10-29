package com.tiger.job.core.runAndEnd;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringBootConfiguration;

/**
 * 项目启动前后的配置
 *
 * @author huxuehao
 **/
@SpringBootConfiguration
public class RunAndEnd implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        System.out.println("*****************************************************************************************");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("* * * * * * * * * * * * * * Application successfully started * * * * *  * * * * * * * * *");
        System.out.println("* * * * * * * * * * * * * * * * * * Welcome to DCSJOB * * * * * * * * * * * * * * * * * *");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("*****************************************************************************************");
    }
}
