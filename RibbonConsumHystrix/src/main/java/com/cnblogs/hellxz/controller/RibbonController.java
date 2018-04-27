package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.servcie.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : Hellxz
 * @Description: Ribbon消费者controller
 * @Date : 2018/4/20 10:07
 */
@RestController
@RequestMapping("hystrix")
public class RibbonController {

    @Autowired
    RibbonService service;

    @GetMapping("/invoke")
    public String helloHystrix(){
        //调用服务层方法
        return service.helloService();
    }

    /**
     * 发送同步请求，使用继承方式实现自定义Hystrix
     */
    @GetMapping("/sync")
    public User sendSyncRequestGetUser(){
        return service.useSyncRequestGetUser();
    }

    /**
     * 发送异步请求，使用继承方式实现自定义Hystrix
     */
    @GetMapping("/async")
    public User sendAsyncRequestGetUser(){
        return service.useAsyncRequestGetUser();
    }
}
