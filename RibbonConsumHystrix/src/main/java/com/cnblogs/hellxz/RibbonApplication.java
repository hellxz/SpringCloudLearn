package com.cnblogs.hellxz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author : Hellxz
 * @Description: Hystrix熔断器使用HelloWorld
 * @Date : 2018/4/20 10:03
 */
@SpringCloudApplication
@ServletComponentScan //自定义Filter中用到
//@EnableCircuitBreaker //开启被dashboard监控，该注解在@SpringCloudApplication中存在
public class RibbonApplication {

    @Bean
    @LoadBalanced
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }
}
