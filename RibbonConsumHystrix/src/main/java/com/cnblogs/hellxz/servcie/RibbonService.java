package com.cnblogs.hellxz.servcie;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.hystrix.UserCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @Author : Hellxz
 * @Description: Ribbon服务层
 * @Date : 2018/4/26 10:08
 */
@Service
public class RibbonService {

    private static final Logger logger = Logger.getLogger(RibbonService.class);
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用Hystrix注解，声明回调类，此方法为同步请求，如果不指定回调方法会使用默认
     */
    @HystrixCommand(fallbackMethod = "hystrixFallback")
    public String helloService(){
        long start = System.currentTimeMillis();
        //设置随机3秒内延迟，hystrix默认延迟2秒未返回则熔断，调用回调方法
        int sleepMillis = new Random().nextInt(3000);
        logger.info("----sleep-time:"+sleepMillis);

        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //调用服务提供者接口，正常则返回hello字符串
        String body = restTemplate.getForEntity("http://eureka-service/hello", String.class).getBody();
        long end = System.currentTimeMillis();
        logger.info("----spend-time:"+(end-start));
        return body;
    }

    /**
     * 使用注解实现异步请求调用
     *
     * 注意：此处AsyncResult为netfix实现，spring也做了实现，注意导包。
     */
    @HystrixCommand(fallbackMethod = "fallbackForUserTypeReturnMethod")
    public Future<User> asyncRequest(){
        return new AsyncResult<User>(){
            public User invoke(){
                return restTemplate.getForObject("http://eureka-service/user", User.class);
            }
        };
    }

    /**
     * 调用服务失败处理方法：返回类型为User
     */
    public User fallbackForUserTypeReturnMethod(){
        return null;
    }

    /**
     * 调用服务失败处理方法：返回类型为字符串
     * @return “error"
     */
    public String hystrixFallback(){
        return "error";
    }

    /**
     * 使用自定义HystrixCommand同步方法调用接口
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
     * 使用自定义HystrixCommand异步方法调用接口
     */
    public User useAsyncRequestGetUser(){

        Future<User> userFuture = new UserCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("")).andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                restTemplate,0L).queue();

        User userAsync = null;

        try {
            //获取Future内部包含的对象，可以设置延迟时间
            userAsync = userFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return userAsync;
    }

    /**
     * 注解实现Observable响应式开发
     */
    @HystrixCommand
    public Observable<User> observeByAnnotation() {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    User user = restTemplate.getForObject("http://eureka-service/user", User.class);
                    subscriber.onNext(user);
                    subscriber.onCompleted();
                }
            }
        });
    }

}
