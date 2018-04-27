package com.cnblogs.hellxz.servcie;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;


/**
 * @Author : Hellxz
 * @Description: Ribbon服务层
 * @Date : 2018/4/20 10:08
 */
@Service
public class RibbonService {

    private static final Logger logger = Logger.getLogger(RibbonService.class);
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hystrixFallback")
    public String helloService(){
        long start = System.currentTimeMillis();
        //设置随机延迟，hystrix默认延迟2秒未返回则熔断，调用回调方法
       /* int sleepMillis = new Random().nextInt(3000);
        logger.info("--sleep-time:"+sleepMillis);
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //调用服务提供者接口，正常则返回hello字符串
        String body = restTemplate.getForEntity("http://eureka-service/hello", String.class).getBody();
        long end = System.currentTimeMillis();
        logger.info("--spend-time:"+(end-start));
        return body;
    }

    /**
     * 调用服务失败处理方法
     * @return “error"
     */
    public String hystrixFallback(){
        return "error";
    }
}
