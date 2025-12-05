package com.mistakenotebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 错题本管理系统启动类
 * 
 * @author simon
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.mistakenotebook",
                "com.adminpro"
        },
        exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class}
)
@EnableCaching
public class MistakeNotebookApplication {

    public static void main(String[] args) {
        SpringApplication.run(MistakeNotebookApplication.class, args);
    }
}
