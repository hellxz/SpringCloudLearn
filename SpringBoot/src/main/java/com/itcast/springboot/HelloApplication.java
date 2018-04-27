package com.itcast.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.*;
import javax.xml.ws.RequestWrapper;

/**
 * @Author : Hellxz
 * @Description:
 * @Date : 2018/4/4 13:19
 */
@Controller
@SpringBootApplication
public class HelloApplication{

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello world";
    }
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class,args);
    }
}
