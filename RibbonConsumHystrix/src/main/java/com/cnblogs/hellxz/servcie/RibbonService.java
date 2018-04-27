package com.cnblogs.hellxz.servcie;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.hystrix.UserCommand;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * @Author : Hellxz
 * @Description: Ribbon服务层
 * @Date : 2018/4/20 10:08
 */
@Service
public class RibbonService {

    private static final Logger logger = Logger.getLogger(RibbonService.class);
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hystrixFallback")
    public String helloService(){
        long start = System.currentTimeMillis();
        //设置随机延迟，hystrix默认延迟2秒未返回则熔断，调用回调方法
       /* int sleepMillis = new Random().nextInt(3000);
        logger.info("--sleep-time:"+sleepMillis);
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //调用服务提供者接口，正常则返回hello字符串
        String body = restTemplate.getForEntity("http://eureka-service/hello", String.class).getBody();
        long end = System.currentTimeMillis();
        logger.info("--spend-time:"+(end-start));
        return body;
    }

    /**
     * 调用服务失败处理方法
     * @return “error"
     */
    public String hystrixFallback(){
        return "error";
    }

    /**
     * 使用同步方法调用接口
     */
    public User useSyncRequestGetUser(){
        //这里使用Spring注入的RestTemplate, Spring注入的对象都是静态的
        User userSync = new UserCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("")).andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                restTemplate ,0L).execute();

        return userSync;
    }

    /**
     * 使用异步方法调用接口
     */
    public User useAsyncRequestGetUser(){

        Future<User> userFuture = new UserCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("")).andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                restTemplate,0L).queue();

        User userAsync = null;

        try {
            //获取Future内部包含的对象
            userAsync = userFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return userAsync;
    }
}
