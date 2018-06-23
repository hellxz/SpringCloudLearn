package com.cnblogs.hellxz.config;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * 用于禁用Feign中Hystrix的配置
 */
public class DisableHystrixConfiguration {

/*    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder(){
        return Feign.builder();
    }*/
}
