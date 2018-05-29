package com.cnblogs.hellxz.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 服务提供者的Feign
 * 这个接口相当于把原来的服务提供者项目当成一个Service类，
 * 我们只需在声明它的Feign-client的名字，会自动去调用注册中心的这个名字的服务
 * 更简单的理解是value相当于MVC中的Controller类的父路径，通过"父路径+子路径和参数来调用服务"
 */
@FeignClient(value = "eureka-service")
public interface EurekaServiceFeign {

    @RequestMapping(value = "/hello", method=RequestMethod.GET)
    String helloFeign();
}
