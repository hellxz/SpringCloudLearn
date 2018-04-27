package com.cnblogs.hellxz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author : Hellxz
 * @Description: EurekaClient
 * @Date : 2018/4/13 16:57
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServiceApplication.class, args);
	}
}
