package com.cnblogs.hellxz.controller;


import com.cnblogs.hellxz.client.EurekaServiceFeign;
import com.cnblogs.hellxz.client.RefactorServiceFeign;
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
    private RefactorServiceFeign refactorServiceFeign; //注入实现服务提供者的Feign的实现类

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

    @GetMapping("/headers")
    @ResponseBody
    public String getParamByHeaders(@RequestHeader("name") String name){
        return eurekaServiceFeign.getParamByHeaders(name);
    }

    @PostMapping("/requestBody")
    @ResponseBody
    public User getParamByRequestBody(@RequestBody User user){
        return eurekaServiceFeign.getUserByRequestBody(user);
    }

    @GetMapping("/refactorFeign")
    @ResponseBody
    public String refactorParentFeign(){
        return refactorServiceFeign.helloFeign();
    }
}
