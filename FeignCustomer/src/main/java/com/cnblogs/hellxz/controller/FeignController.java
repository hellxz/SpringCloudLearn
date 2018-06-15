package com.cnblogs.hellxz.controller;


import com.cnblogs.hellxz.client.EurekaServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("feign")
public class FeignController {

    @Autowired
    private EurekaServiceFeign eurekaServiceFeign;

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return eurekaServiceFeign.helloFeign();
    }
}
