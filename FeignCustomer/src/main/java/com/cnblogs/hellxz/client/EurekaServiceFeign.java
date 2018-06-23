package com.cnblogs.hellxz.client;

import com.cnblogs.hellxz.config.DisableHystrixConfiguration;
import com.cnblogs.hellxz.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务提供者的Feign
 * 这个接口相当于把原来的服务提供者项目当成一个Service类，
 * 我们只需在声明它的Feign-client的名字，会自动去调用注册中心的这个名字的服务
 * 更简单的理解是value相当于MVC中的Controller类的父路径，通过"父路径+子路径和参数来调用服务"
 */
//其中的value的值为要调用服务的名称, configuration中配置的是禁用Feign中的Hystrix功能的类
//如需使用Hystrix功能，请去掉configuration中的禁用Hystrix的配置类
@FeignClient(value = "eureka-service",configuration = DisableHystrixConfiguration.class, fallback = EurekaServiceFeignFallback.class)
public interface EurekaServiceFeign {

    /**
     * 第一个Feign代码
     * Feign中没有原生的@GetMapping/@PostMapping/@DeleteMapping/@PutMapping，要指定需要用method进行
     */
    @RequestMapping(value = "/hello", method=RequestMethod.GET)
    String helloFeign();

    /**
     * 在服务提供者我们有一个方法是用直接写在链接，SpringMVC中用的@PathVariable
     * 这里边和SpringMVC中有些有一点点出入，SpringMVC中只有一个参数而且参数名的话是不用额外指定参数名的，而feign中必须指定
     */
    @RequestMapping(value = "/greet/{dd}",method = RequestMethod.GET)
    String greetFeign(@PathVariable("dd") String dd);

    /**
     * 这里说下@RequestParam 注解和SpringMVC中差别也是不大，我认为区别在于Feign中的是参数进入URL或请求体中，
     * 而SpringMVC中是参数从请求体中到方法中
     * @param ids id串，比如“1，2，3”
     */
    @RequestMapping(value = "/users",method = RequestMethod.GET)
    List<User> getUsersByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 这里是将参数添加到Headers中
     * @param name 参数
     */
    @RequestMapping(value = "/headers")
    String getParamByHeaders(@RequestHeader("name") String name);

    /**
     * 调用服务提供者的post方法,接收回来再被服务提供者丢回来
     * @param user User对象
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    User getUserByRequestBody(@RequestBody User user);

    /**
     * 测试重连机制
     */
    @RequestMapping(value = "/retry", method = RequestMethod.GET)
    String feignRetry();
}