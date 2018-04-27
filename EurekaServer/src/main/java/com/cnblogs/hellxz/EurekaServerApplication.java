package com.cnblogs.hellxz;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author : Hellxz
 * @Description: EurekaServer
 * @Date : 2018/4/13 16:53
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {
		//启动这个springboot应用
		SpringApplication.run(EurekaServerApplication.class,args);
	}
}
