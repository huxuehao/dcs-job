package com.tiger.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages="com.tiger.job.**.mapper")
public class Application {
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
