package com.cnblogs.hellxz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @Author : Hellxz
 * @Description: 仪表盘 启动主类
 * @Date : 2018/5/4 17:55
 */

@SpringBootApplication
@EnableHystrixDashboard
public class DashBoardApp {

    public static void main(String[] args) {
        SpringApplication.run(DashBoardApp.class, args);
    }
}
