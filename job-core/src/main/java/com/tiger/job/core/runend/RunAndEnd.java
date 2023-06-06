package com.tiger.job.core.runend;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringBootConfiguration;

/**
 * @ClassName RunAndEnd
 * @Description TODO
 * @Author huxuehao
 **/
@SpringBootConfiguration
public class RunAndEnd implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        System.out.println("-------------------------------------------");
        System.out.println("|                                         |");
        System.out.println("|       dsc-job successfully started      |");
        System.out.println("|                                         |");
        System.out.println("-------------------------------------------");
    }
}
