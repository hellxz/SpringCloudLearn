package com.cnblogs.hellxz.hystrix;

import com.cnblogs.hellxz.entity.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.web.client.RestTemplate;

/**
 * @Author : Hellxz
 * @Description:
 * @Date : 2018/4/20 14:47
 */
public class UserCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;
    private Long id;

    public UserCommand(Setter setter, RestTemplate restTemplate, Long id){
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected User run() {
        return restTemplate.getForObject("http://localhost:8080/user", User.class);
    }

    public static void main(String[] args) {
        //同步请求
        User u=new UserCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("")).andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                new RestTemplate(),0L).execute();

        System.out.println(u);
    }

}
