package com.gcf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.gcf.mapper")
@EnableScheduling
public class GCFUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(GCFUserApplication.class,args);
    }
}