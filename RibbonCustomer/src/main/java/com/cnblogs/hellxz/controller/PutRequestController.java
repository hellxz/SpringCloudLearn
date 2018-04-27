package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @Author : Hellxz
 * @Description: put请求、delete请求，重载的参数与上述demo基本相同，不予列出
 * @Date : 2018/4/19 13:43
 */
@RestController
public class PutRequestController {

    private Logger logger = Logger.getLogger(PostRequestController.class);
    @Autowired
    private RestTemplate restTemplate;

    /**
     * put请求示例，一般put请求多用作修改
     */
    @PutMapping("/put")
    public void put(@RequestBody User user){
        restTemplate.put("http://eureka-service/put",user);
    }

    /**
     * delete请求示例
     */
    @DeleteMapping("/del/{id}")
    public void delete(@PathVariable Long id){
        restTemplate.delete("http://eureka-service/delete/{1}", id);
    }
}
