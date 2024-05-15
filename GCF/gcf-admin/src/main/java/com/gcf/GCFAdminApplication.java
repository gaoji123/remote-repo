package com.gcf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gcf.mapper")

public class GCFAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(GCFAdminApplication.class,args);
    }
}