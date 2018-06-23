package com.cnblogs.hellxz.client;

import com.cnblogs.hellxz.interfaces.HelloService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 继承服务提供者的HelloService的接口，从而拥有这个接口的所有方法
 * 那么在这个Feign中就只需要使用HelloService定义的接口方法
 */
@FeignClient("eureka-service")
public interface RefactorHelloServiceFeign extends HelloService {
}
