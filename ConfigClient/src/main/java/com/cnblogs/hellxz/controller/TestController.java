package com.cnblogs.hellxz.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope //开启更新功能
@RequestMapping("api")
public class TestController {

    @Value("${from}")
    private String fromValue;

    /**
     * 返回配置文件中的值
     */
    @GetMapping("/from")
    @ResponseBody
    public String returnFormValue(){
        return fromValue;
    }
}
