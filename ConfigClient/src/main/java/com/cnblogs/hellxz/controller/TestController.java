package com.cnblogs.hellxz.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {

    @Value("${from}")
    private String fromValue;

    @GetMapping("/fromValue")
    @ResponseBody
    public String returnFormValue(){
        return fromValue;
    }
}
