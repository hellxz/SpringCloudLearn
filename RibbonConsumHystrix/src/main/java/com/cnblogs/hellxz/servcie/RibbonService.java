package com.cnblogs.hellxz.servcie;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.hystrix.CacheCommand;
import com.cnblogs.hellxz.hystrix.UserCollapseCommand;
import com.cnblogs.hellxz.hystrix.UserCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * @Author : Hellxz
 * @Description: Ribbon服务层
 * @Date : 2018/4/26 10:08
 */
@Service
public class RibbonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RibbonService.class);
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
        LOGGER.info("----sleep-time:"+sleepMillis);

        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //调用服务提供者接口，正常则返回hello字符串
        String body = restTemplate.getForEntity("http://eureka-service/hello", String.class).getBody();
        long end = System.currentTimeMillis();
        LOGGER.info("----spend-time:"+(end-start));
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
        LOGGER.error("捕获到异常： "+e.getMessage());
        return null;
    }
//====================================================================
    /**
     * 通过@HystrixCommand 注解 设置组名、命令名、线程池名
     * 当然也可以写fallbackMethod,举例就不写了
     */
    @HystrixCommand(groupKey = "groupKey",commandKey = "commandKey", threadPoolKey = "threadPool")
    public User hystrixCommandComment(){

        return restTemplate.getForObject("http://eureka-service/user", User.class);
    }

//====================================================================
    /**
     * 继承方式开启请求缓存,注意commandKey必须与清除的commandKey一致
     */
    public void openCacheByExtends(){
        CacheCommand command1 = new CacheCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                                                                            HystrixCommandGroupKey.Factory.asKey("group")).andCommandKey(HystrixCommandKey.Factory.asKey("test")),
                                                                        restTemplate,1L);
        CacheCommand command2 = new CacheCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                                                                            HystrixCommandGroupKey.Factory.asKey("group")).andCommandKey(HystrixCommandKey.Factory.asKey("test")),
                                                                        restTemplate,1L);
        Integer result1 = command1.execute();
        Integer result2 = command2.execute();
        LOGGER.info("first request result is:{} ,and secend request result is: {}", result1, result2);
    }

    /**
     * 继承方式清除请除缓存
     */
    public void clearCacheByExtends(){
        CacheCommand.flushRequestCache(1L);
        LOGGER.info("请求缓存已清空！");
    }

    /**
     * 使用注解请求缓存 方式1
     * @CacheResult  标记这是一个缓存方法，结果会被缓存
     */
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(commandKey = "commandKey1")
    public Integer openCacheByAnnotation1(Long id){
        //此次结果会被缓存
        return restTemplate.getForObject("http://eureka-service/hystrix/cache", Integer.class);
    }

    /**
     * 使用注解清除缓存 方式1
     * @CacheRemove 必须指定commandKey才能进行清除指定缓存
     */
    @CacheRemove(commandKey = "commandKey1", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public void flushCacheByAnnotation1(Long id){
        LOGGER.info("请求缓存已清空！");
        //这个@CacheRemove注解直接用在更新方法上效果更好
    }

    /**
     * 第一种方法没有使用@CacheKey注解，而是使用这个方法进行生成cacheKey的替换办法
     * 这里有两点要特别注意：
     * 1、这个方法的入参的类型必须与缓存方法的入参类型相同，如果不同被调用会报这个方法找不到的异常
     * 2、这个方法的返回值一定是String类型
     */
    public String getCacheKey(Long id){
        return String.valueOf(id);
    }

    /**
     * 使用注解请求缓存 方式2
     * @CacheResult  标记这是一个缓存方法，结果会被缓存
     * @CacheKey 使用这个注解会把最近的参数作为cacheKey
     *
     * 注意：有些教程中说使用这个可以指定参数，比如：@CacheKey("id") , 但是我这么用会报错，网上只找到一个也出这个错误的贴子没解决
     *          而且我发现有一个问题是有些文章中有提到 “不使用@CacheResult，只使用@CacheKey也能实现缓存” ，经本人实测无用
     */
    @CacheResult
    @HystrixCommand(commandKey = "commandKey2")
    public Integer openCacheByAnnotation2(@CacheKey Long id){
        //此次结果会被缓存
        return restTemplate.getForObject("http://eureka-service/hystrix/cache", Integer.class);
    }

    /**
     * 使用注解清除缓存 方式2
     * @CacheRemove 必须指定commandKey才能进行清除指定缓存
     */
    @CacheRemove(commandKey = "commandKey2")
    @HystrixCommand
    public void flushCacheByAnnotation2(@CacheKey Long id){
        LOGGER.info("请求缓存已清空！");
        //这个@CacheRemove注解直接用在更新方法上效果更好
    }

    /**
     * 使用注解请求缓存 方式3
     * @CacheResult  标记这是一个缓存方法，结果会被缓存
     * @CacheKey 使用这个注解会把最近的参数作为cacheKey
     *
     * 注意：有些教程中说使用这个可以指定参数，比如：@CacheKey("id") , 但是我这么用会报错，网上只找到一个也出这个错误的贴子没解决
     *          而且我发现有一个问题是有些文章中有提到 “不使用@CacheResult，只使用@CacheKey也能实现缓存” ，经本人实测无用
     */
    @CacheResult
    @HystrixCommand(commandKey = "commandKey3")
    public Integer openCacheByAnnotation3(Long id){
        //此次结果会被缓存
        return restTemplate.getForObject("http://eureka-service/hystrix/cache", Integer.class);
    }

    /**
     * 使用注解清除缓存 方式3
     * @CacheRemove 必须指定commandKey才能进行清除指定缓存
     */
    @CacheRemove(commandKey = "commandKey3")
    @HystrixCommand
    public void flushCacheByAnnotation3(Long id){
        LOGGER.info("请求缓存已清空！");
        //这个@CacheRemove注解直接用在更新方法上效果更好
    }


//===================================================================

    /**请求合并使用到的测试方法**/

    /**
     * 查一个User对象
     */
    public User findOne(Long id){
        LOGGER.info("findOne方法执行了，id= "+id);
        return restTemplate.getForObject("http://eureka-service/users/{1}", User.class, id);
    }

    /**
     * 查多个对象
     *
     * 注意： 这里用的是数组，作为结果的接收，因为restTemplate.getForObject方法在这里受限
     *         如果尽如《SpringCloud微服务实战》一书中指定类型为List.class，会返回一个List<LinkedHashMap>类型的集合
     *         为了避坑这里我们使用数组的方式接收结果
     */
    public List<User> findAll(List<Long> ids){
        LOGGER.info("findAll方法执行了，ids= "+ids);
        User[] users = restTemplate.getForObject("http://eureka-service/users?ids={1}", User[].class, StringUtils.join(ids, ","));
        return Arrays.asList(users);
    }

    /**注解方式实现请求合并**/

    /**
     * 被合并请求的方法
     * 注意是timerDelayInMilliseconds，注意拼写
     */
    @HystrixCollapser(batchMethod = "findAllByAnnotation",collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds",value = "100")})
    public Future<User> findOneByAnnotation(Long id){
        //你会发现根本不会进入这个方法体
        LOGGER.info("findOne方法执行了，ids= "+id);
        return null;
    }

    /**
     * 真正执行的方法
     */
    @HystrixCommand
    public List<User> findAllByAnnotation(List<Long> ids){
        LOGGER.info("findAll方法执行了，ids= "+ids);
        User[] users = restTemplate.getForObject("http://eureka-service/users?ids={1}", User[].class, StringUtils.join(ids, ","));
        return Arrays.asList(users);
    }
}
