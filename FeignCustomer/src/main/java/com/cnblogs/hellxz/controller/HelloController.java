package com.cnblogs.hellxz.controller;


import com.cnblogs.hellxz.client.EurekaServiceFeign;
import com.cnblogs.hellxz.client.RefactorHelloServiceFeign;
import com.cnblogs.hellxz.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("feign")
public class HelloController {

    @Autowired
    private EurekaServiceFeign eurekaServiceFeign; //注入Feign

    @Autowired
    private RefactorHelloServiceFeign refactorHelloServiceFeign;

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello(){
        //在方法中调用feign的方法
        return eurekaServiceFeign.helloFeign();
    }

    /**
     * 注意这里是SpringMVC，URL中的参数与方法中的参数名相同无需在注解中注明参数名
     */
    @GetMapping("/greet/{test}")
    @ResponseBody
    public String greet(@PathVariable String test){
        return eurekaServiceFeign.greetFeign(test);
    }

    /**
     * 调用Feign中使用@RequestParam的方法
     */
    @GetMapping("/users")
    @ResponseBody
    public List<User> getUserListByIds(@RequestParam("ids") List<Long> ids){
      return eurekaServiceFeign.getUsersByIds(ids);
    }

    /**
     * 调用Feign使用headers传参
     */
    @GetMapping("/headers")
    @ResponseBody
    public String getParamByHeaders(@RequestHeader("name") String name){
        return eurekaServiceFeign.getParamByHeaders(name);
    }

    /**
     * 调用Feign post传递对象
     */
    @PostMapping("/requestBody")
    @ResponseBody
    public User getParamByRequestBody(@RequestBody User user){
        return eurekaServiceFeign.getUserByRequestBody(user);
    }

    /**
     * 测试Feign的继承特性
     */
    @GetMapping("/refactor/{name}")
    @ResponseBody
    public String refactorHelloService(@PathVariable String name){
        return refactorHelloServiceFeign.hello(name);
    }

    /**
     * 测试重连机制
     */
    @GetMapping("/retry")
    @ResponseBody
    public String retry(){
        return eurekaServiceFeign.feignRetry();
    }
}
