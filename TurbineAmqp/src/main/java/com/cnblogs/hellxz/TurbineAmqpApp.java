package com.cnblogs.hellxz;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

/**
 * <b>类名</b>: TurbineAmqpApp
 * <p><b>描    述</b>: Turbine基于RabbitMQ消息代理的主类</p>
 *
 * <p><b>创建日期</b>2018/5/28 16:29</p>
 * @author HELLXZ 张
 * @version 1.0
 * @since jdk 1.8
 */
@SpringCloudApplication
@EnableTurbineStream
public class TurbineAmqpApp {

    public static void main(String[] args) {
        SpringApplication.run(TurbineAmqpApp.class, args);
    }
}
