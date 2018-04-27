package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


/**
 * @Author : Hellxz
 * @Description:
 * @Date : 2018/4/18 10:21
 */
@RestController
public class PostRequestController {

    private Logger logger = Logger.getLogger(PostRequestController.class);

    /**
     * 接收一个对象再返回回去,postForEntity/postForObject方法通用
     */
    @PostMapping("/user")
    public User returnUserByPost(@RequestBody User user){
        logger.info("/use接口 "+user);
        if(user == null) return new User("这是一个空对象","","");
        return user;
    }

    /**
     * 测试PostForEntity方法的参数，可以直接看输出判断结果了
     */
    @PostMapping("/user/{str}")
    public User returnUserByPost(@PathVariable String str, @RequestBody User user){
        logger.info("/user/someparam 接口传参 name："+str +" "+user);
        if(user == null) return new User("这是一个空对象","","");
        return user;
    }

    /**
     * 为postForLocation方法返回URI
     */
    @PostMapping("/location")
    public URI returnURI(@RequestBody User user){
        //这里模拟一个url，真实资源位置不一定是这里
        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://hello-service/location")
                                                                                                .build().expand(user).encode();
        URI toUri = uriComponents.toUri();
        //这里不知道是什么问题，明明生成uri了，返回之后好像并没有被获取到
        logger.info("/location uri:"+toUri);
        return toUri;
    }

}
