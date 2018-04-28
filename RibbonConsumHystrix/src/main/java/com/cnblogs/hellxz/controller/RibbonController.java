package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.servcie.RibbonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author : Hellxz
 * @Description: Ribbon消费者controller
 * @Date : 2018/4/20 10:07
 */
@RestController
@RequestMapping("hystrix")
public class RibbonController {

    public static final Logger logger = LoggerFactory.getLogger(RestController.class);

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

    /**
     * 使用注解发送异步请求
     */
    @GetMapping("/annotationasync")
    public User sendAsyncRequestByAnnotation(){
        Future<User> userFuture = service.asyncRequest();
        try {
            return userFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 使用注解发送异步请求
     */
    @GetMapping("/observe")
    public void sendAsyncRequestByObserve(){
        Observable<User> userObservable = service.observeByAnnotation();
        logger.info("----------------------Observe对象："+userObservable);
        //这里只是简单拿到了这个对象，对此本人没有深入，有兴趣可以研究
    }

}
