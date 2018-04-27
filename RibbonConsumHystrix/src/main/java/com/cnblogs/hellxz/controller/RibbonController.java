package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.servcie.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : Hellxz
 * @Description: Ribbon消费者controller
 * @Date : 2018/4/20 10:07
 */
@RestController
public class RibbonController {

    @Autowired
    RibbonService service;

    @GetMapping("/hystrix/test")
    public String helloHystrix(){
        //调用服务层方法
        return service.helloService();
    }

}
