package com.cnblogs.hellxz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author : Hellxz
 * @Description: 消费应用
 * @Date : 2018/4/16 15:45
 */
//@EnableCircuitBreaker  //启动熔断器hystrix
//@EnableDiscoveryClient
//@SpringBootApplication
@SpringCloudApplication  //标准springcloud应用包含上述三个注解
public class CustomerApplication {

    @Bean  //将此Bean交给spring容器
    @LoadBalanced  //通过此注解开启负载均衡
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
