package com.mistakenotebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 错题本管理系统启动类
 * 
 * @author simon
 */
@SpringBootApplication(scanBasePackages = {
        "com.mistakenotebook",
        "com.adminpro"
})
@EnableScheduling
public class MistakeNotebookApplication {

    public static void main(String[] args) {
        SpringApplication.run(MistakeNotebookApplication.class, args);
    }
}
