package com.cnblogs.hellxz.hystrix;

import com.cnblogs.hellxz.entity.User;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author : Hellxz
 * @Description:
 * @Date : 2018/4/20 14:47
 */
public class UserCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;
    private static Long id;

    public UserCommand(Setter setter, RestTemplate restTemplate, Long id){
        super(setter);
        this.restTemplate = restTemplate;
        UserCommand.id = id;
    }

    //=====================HystrixCommand构造方法=========================
    /**
     * 扩展：Setter对象是用于设置参数的，比如，我们可以通过它来划分线程池，可以设置组名Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("组名字段"))
     * 设置命令名Setter.andCommandKey(HystrixCommandKey.Factory.asKey("命令名"))
     * 设置线程池Setter.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("线程池名"))
     * 其中CommandKey是可选的，GroupKey是必要的，Hystrix会通过组名来组织和统计命令的告警和仪表盘信息，在没有指定线程池的时候，默认会将相同组名的命令使用同一个线程池，
     * 我们可以通过设置组名来实现线程池的划分;指定线程池的话，以线程池名划分线程池
     *
     * 下边举几个例子，重载UserCommand的构造方法，注意：上边的构造方法是为了初始化成员变量，本质与只有Setter的构造方法相同
     * 由下几个例子我们可以发现Setter包含了组名、命令名、线程池名、超时时间，详情可以看源码
     */
    //只有setter的构造方法
    public UserCommand(Setter setter){
        super(setter);
    }
    //只设置组名的构造方法
    public UserCommand(HystrixCommandGroupKey group) {
        super(group);
    }
    //设置组名、线程池名
    public UserCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool) {
        super(group,threadPool);
    }
    //设置组名和执行超时时间
    public UserCommand(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group,executionIsolationThreadTimeoutInMilliseconds);
    }
    //设置组名、命令名、线程池名、执行超时时间
    public UserCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group,threadPool,executionIsolationThreadTimeoutInMilliseconds);
    }

    /**
     * 注意本地main方法启动，url请用http://localhost:8080/user
     * 通过controller请求启动需要改为服务调用地址：http://eureka-service/user
     */
    @Override
    protected User run() {
        //本地请求
//        return restTemplate.getForObject("http://localhost:8080/user", User.class);
        //连注册中心请求
        return restTemplate.getForObject("http://eureka-service/user", User.class);
    }

    public static Long getId() {
        return id;
    }

    /**
     * 此方法为《spirngcloud微服务实战》中的学习部分，仅用于在此项目启动的之后调用本地服务,但是不能没有走注册中心。
     * 书中为我们留下了这个坑，详情请直接翻阅151页。
     * 问题解决请参考：https://blog.csdn.net/lvyuan1234/article/details/76550706
     * 本人在书中基础上已经完成调用注册中心服务的功能，见RibbonService类中具体实现
     */
    /**
    public static void main(String[] args) {
        //初始化请求上下文，这一行只在main方法中维持之前代码的可用
        HystrixRequestContext.initializeContext();

        //同步请求
        User userSync=new UserCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("")).andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                new RestTemplate(),0L).execute();

        System.out.println("------------------This is sync request's response:"+userSync);

        //异步请求
        Future<User> userFuture = new UserCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("")).andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                new RestTemplate(),0L).queue();
        User userAsync = null;
        try {
            userAsync = userFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("------------------This is async request's response:"+userAsync);

        //observe和toObservable方法
        UserCommand userCommand = new UserCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")), new RestTemplate(),1L);
        Observable<User> observe = userCommand.observe();
        System.out.println("------------------This is observe's response:"+observe);
        Observable<User> userObservable = userCommand.toObservable();
        System.out.println("------------------This is toObserve's response:"+userObservable);
    }*/

}
