package com.cnblogs.hellxz.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <b>类名</b>: HelloService
 * <p><b>描    述</b>: 用于定义Controller的接口，实现放在Controller中
 * 这个类的主要作用是方便Feign中继承特性的测试
 * </p>
 *
 * <p><b>创建日期</b> 2018/6/23 11:43 </p>
 * @author  HELLXZ 张
 * @version  1.0
 * @since  jdk 1.8
 */
public interface HelloService {

    /**
     * 声明一个接口，没有实现
     */
    @GetMapping(value = "/refactor-service/{name}")
    String hello(@PathVariable("name") String name);
}
