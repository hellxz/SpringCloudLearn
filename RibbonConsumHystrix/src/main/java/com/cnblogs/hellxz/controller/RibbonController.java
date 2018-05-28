package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.hystrix.UserCollapseCommand;
import com.cnblogs.hellxz.servcie.RibbonService;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
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

    public static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);

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
        //初始化请求上下文，这一行只在main方法中维持之前代码的可用
        HystrixRequestContext.initializeContext();
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
        LOGGER.info("----------------------Observe对象："+userObservable);
        //这里只是简单拿到了这个对象，对此本人没有深入，有兴趣可以研究
    }

    /**
     * 使用自定义HystrixCommand的默认fallback方法
     */
    @GetMapping("/fallback")
    public User fallback(){
        return service.defaultCommandFallback();
    }

    //========================================================
    /**
     * 继承方式开启请求缓存,并多次调用CacheCommand的方法
     * 在两次请求之间加入清除缓存的方法
     */
    @GetMapping("/cacheOn")
    public void openCacheTest(){
        //初始化Hystrix请求上下文
//        HystrixRequestContext.initializeContext();
        //开启请求缓存并测试两次
        service.openCacheByExtends();
        //清除缓存
        service.clearCacheByExtends();
        //再次开启请求缓存并测试两次
        service.openCacheByExtends();
    }


    /**
     * 注解方式请求缓存，第一种
     */
    @GetMapping("/cacheAnnotation1")
    public void openCacheByAnnotation1(){
        //初始化Hystrix请求上下文
//        HystrixRequestContext.initializeContext();
        //访问并开启缓存
        Integer result1 = service.openCacheByAnnotation1(1L);
        Integer result2 = service.openCacheByAnnotation1(1L);
        LOGGER.info("first request result is:{} ,and secend request result is: {}", result1, result2);
        //清除缓存
        service.flushCacheByAnnotation1(1L);
        //再一次访问并开启缓存
        Integer result3 = service.openCacheByAnnotation1(1L);
        Integer result4 = service.openCacheByAnnotation1(1L);
        LOGGER.info("first request result is:{} ,and secend request result is: {}", result3, result4);
    }

    /**
     * 注解方式请求缓存，第二种
     */
    @GetMapping("/cacheAnnotation2")
    public void openCacheByAnnotation2(){
        //初始化Hystrix请求上下文
//        HystrixRequestContext.initializeContext();
        //访问并开启缓存
        Integer result1 = service.openCacheByAnnotation2(2L);
        Integer result2 = service.openCacheByAnnotation2(2L);
        LOGGER.info("first request result is:{} ,and secend request result is: {}", result1, result2);
        //清除缓存
        service.flushCacheByAnnotation2(2L);
        //再一次访问并开启缓存
        Integer result3 = service.openCacheByAnnotation2(2L);
        Integer result4 = service.openCacheByAnnotation2(2L);
        LOGGER.info("first request result is:{} ,and secend request result is: {}", result3, result4);
    }

    /**
     * 注解方式请求缓存，第三种
     */
    @GetMapping("/cacheAnnotation3")
    public void openCacheByAnnotation3(){
        //初始化Hystrix请求上下文
//        HystrixRequestContext.initializeContext();
        //访问并开启缓存
        Integer result1 = service.openCacheByAnnotation3(3L);
        Integer result2 = service.openCacheByAnnotation3(3L);
        LOGGER.info("first request result is:{} ,and secend request result is: {}", result1, result2);
        //清除缓存
        service.flushCacheByAnnotation3(3L);
        //再一次访问并开启缓存
        Integer result3 = service.openCacheByAnnotation3(3L);
        Integer result4 = service.openCacheByAnnotation3(3L);
        LOGGER.info("first request result is:{} ,and secend request result is: {}", result3, result4);
    }


    /**请求合并测试**/

    /**
     * 单个请求处理
     * @param id
     */
    @GetMapping("/users/{id}")
    public User findOne(@PathVariable Long id){
        LOGGER.debug("=============/hystrix/users/{} 执行了", id);
        User user = service.findOne(id);
        return user;
    }

    /**
     * 多个请求处理
     * @param ids id串，使用逗号分隔
     */
    @GetMapping("/users")
    public List<User> findAll(@RequestParam List<Long> ids){
        LOGGER.debug("=============/hystrix/users?ids={} 执行了", ids);
        return service.findAll(ids);
    }

    /**
     * 合并请求测试
     * 说明：这个测试本应在findOne方法中new一个UserCollapseCommand对象进行测试
     *         苦于没有好的办法做并发实验，这里就放在一个Controller中了
     *         我们看到，在这个方法中用了三个UserCollapseCommand对象进行模拟高并发
     */
    @GetMapping("/collapse")
    public List<User> collapseTest(){
        LOGGER.info("==========>collapseTest方法执行了");
        List<User> userList = new ArrayList<>();
        Future<User> queue1 = new UserCollapseCommand(service, 1L).queue();
        Future<User> queue2 = new UserCollapseCommand(service, 2L).queue();
        Future<User> queue3 = new UserCollapseCommand(service, 3L).queue();
        try {
            User user1 = queue1.get();
            User user2 = queue2.get();
            User user3 = queue3.get();
            userList.add(user1);
            userList.add(user2);
            userList.add(user3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * 同步方法测试合并请求
     *
     * 说明：这个方法是用来与上面的方法做类比的，通过这个实验我们发现如果使用同步方法，
     *         那么这个请求合并的作用就没有了，这会给findAll方法造成性能浪费
     */
    @GetMapping("synccollapse")
    public List<User> syncCollapseTest(){
        LOGGER.info("==========>syncCollapseTest方法执行了");
        List<User> userList = new ArrayList<>();
        User user1 = new UserCollapseCommand(service, 1L).execute();
        User user2 = new UserCollapseCommand(service, 2L).execute();
        User user3 = new UserCollapseCommand(service, 3L).execute();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        return userList;
    }

    /**
     * 注解方式的请求合并
     *
     * 这里真想不出怎么去测试 这个方法了，有什么好的并发测试框架请自测吧，如果找到这种神器
     * 请给我发邮件告诉我： hellxz001@foxmail.com
     */
    @GetMapping("/collapsebyannotation/{id}")
    public User collapseByAnnotation(@PathVariable Long id) throws ExecutionException, InterruptedException {
        Future<User> one = service.findOneByAnnotation(id);
        User user = one.get();
        return user;
    }

}
