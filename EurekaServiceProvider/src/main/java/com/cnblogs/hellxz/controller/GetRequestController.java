package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author : Hellxz
 * @Description: 服务提供者
 * @Date : 2018/4/18 11:36
 */
@RestController
public class GetRequestController {

    @Autowired
    private DiscoveryClient client; //注入发现客户端

    private final Logger logger = Logger.getLogger(GetRequestController.class);

    /**
     * go straight test
     */
    @GetMapping(value = "/hello")
    public String hello(){
        //获取服务实例，作用为之后console显示效果
        ServiceInstance serviceInstance = client.getLocalServiceInstance();
        logger.info("/hello host:"+serviceInstance.getHost()+" service_id:" +serviceInstance.getServiceId());
        return "hello";
    }

    /**
     * parameter test
     */
    @GetMapping(value = "/greet/{dd}")
    public String greet(@PathVariable String dd){
        ServiceInstance serviceInstance = client.getLocalServiceInstance();
        logger.info("/hello host:"+serviceInstance.getHost()+" service_id:" +serviceInstance.getServiceId());
        return "hello "+dd;
    }

    /**
     * 返回测试对象
     */
    @GetMapping("/user")
    public User getUser(){
        ServiceInstance serviceInstance = client.getLocalServiceInstance();
        logger.info("/user "+serviceInstance.getHost()+" port:"+serviceInstance.getPort()+" serviceInstanceid:"+serviceInstance.getServiceId());
        return new User("hellxz","male", "123456789");
    }

    /**
     * 根据名称返回对象，这里模拟查数据库操作
     */
    @GetMapping("/user/{name}")
    public User getUserSelect(@PathVariable String name){
        ServiceInstance serviceInstance = client.getLocalServiceInstance();
        logger.info("/user "+serviceInstance.getHost()+" port:"+serviceInstance.getPort()+" serviceInstanceid:"+serviceInstance.getServiceId());
        if(name.isEmpty()){
            return new User();
        }else if(name.equals("hellxz")){
            return new User("hellxz","male", "123456789");
        }else{
            return new User("随机用户","male", "987654321");
        }
    }
}
