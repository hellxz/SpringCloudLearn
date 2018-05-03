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

//=================================================================
    /**
     * 下面我会定义一种特殊情况在假如我们要调用的方法因为网络等原因无法服务，那么会调用specifyFallback方法
     * 但是如果specifyFallback方法也超时了呢？
     * 我们可以像定义方法一样为回调方法来设定第二个回调方法！
     * 这里只是举例说明有这种用法，不必苛责
     * 为了达到预期的效果，这里为方法加入线程睡眠
     *
     * 使用自定义服务降级
     */
    @HystrixCommand(fallbackMethod = "fallback1")
    public User defaultCommandFallback(){
        //睡眠3秒，让回调生效
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return restTemplate.getForObject("http://eureka-service/user", User.class);
    }

    //第一个fallback method
    @HystrixCommand(fallbackMethod = "fallback2")
    public User fallback1(){

        //睡眠3秒，让回调生效
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setName("First fallback method !");
        return user;
    }

    //第二个fallback method
    public User fallback2(){
        User user = new User();
        user.setName("Second fallback method !");
        return user;
    }

//=================================================================
    /**
     * 异常传播
     *
     * 如果HystrixCommand注解修饰的方法抛出异常，除了HystrixBadRequestException方法外的异常都会被认为方法调用失败，
     * 从而执行fallback方法。很多时候我们是不想这样的，HystrixCommand恰好支持忽略指定异常，此处不作调用了
     * 大家看看代码就好
     */
    @HystrixCommand(ignoreExceptions = {InterruptedException.class}, fallbackMethod = "fallback2")
    public User ignoreException(){

        //睡眠3秒，让回调生效
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setName("Ignore exception succeed !");
        return user;
    }

    /**
     * 异常获取
     */
    @HystrixCommand(fallbackMethod = "fallback3")
    public User catchException(){
        throw new RuntimeException("catched this runtime exception !");
    }

    public User fallback3(Throwable e){
        logger.info("捕获到异常： "+e.getMessage());
        return null;
    }

}
