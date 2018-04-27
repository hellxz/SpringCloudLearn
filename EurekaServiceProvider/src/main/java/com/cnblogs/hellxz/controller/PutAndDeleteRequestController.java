package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;


/**
 * @Author : Hellxz
 * @Description: 服务提供者 put&delete请求controller
 * @Date : 2018/4/19 14:11
 */
@RestController
public class PutAndDeleteRequestController {

    private Logger logger = Logger.getLogger(PutAndDeleteRequestController.class);

    @PutMapping("/put")
    public void put(@RequestBody User user){
        logger.info("/put "+user);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        logger.info("/delete id:"+id);
    }
}
