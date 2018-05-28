package com.cnblogs.hellxz;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@EnableTurbine
@SpringCloudApplication
public class TurbineApp {

    public static void main(String[] args) {
        SpringApplication.run(TurbineApp.class, args);
    }
}
