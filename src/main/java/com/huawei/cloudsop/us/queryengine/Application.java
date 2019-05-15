package com.huawei.cloudsop.us.queryengine;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        //System.setProperty("calcite.debug", "true");
        SpringApplication.run(Application.class, args);
    }
}