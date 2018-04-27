package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


/**
 * @Author : Hellxz
 * @Description: Ribbon消费者post请求controller
 * @Date : 2018/4/18 9:47
 */
@RestController
public class PostRequestController {

    private Logger logger = Logger.getLogger(PostRequestController.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType)
     * 其中参数url不多说，Object request如果是不是一个HttpEntity对象，会自动转换为HttpEntity对象，视作完整的body来处理;
     * 如果是HttpEntity对象，那么会被直接当做body处理并且包含header内容。
     * 以下对于重写的方法就不多说了，使用方法大体同getForEntity，如果仅是简单post对象，那么使用不带Object...variables或Map variables的方法即可。
     * postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables)
     * postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
     *
     * 这里详细说下我遇到的坑：
     * 1、其他几个重载方法的最后边的Object...variables和Map variables都是对之前的url进行操作的，
     *     也就是说，在post请求的url中使用占位符进行传参，而如果在url中没有使用占位符，那么这些最后传的参数是无效的！
     * 2、方法中Object request这个对象如果和服务提供者的接收参数类型相同，那么服务提供者仅需使用@RequestBody接收参数即可。
     * 3、如果二者都使用了，这就比较有趣了，需要一边通过@PathVariable注解接收uri中的参数，一边还需要@RequestBody接收对象或RequestParam按字段接收参数！
     * 4、如果报错了，请仔细看看我上边写的三条，并注意服务提供者的参数接收注解的使用等。
     */
    @PostMapping("/entity")
    public User postForEntity(){
        User user = new User("hellxz1","1","678912345");
        ResponseEntity<User> entity = restTemplate.postForEntity("http://eureka-service/user/{str}", user, User.class, "测试参数");
        User body = entity.getBody(); //所有restTemplate.*ForEntity方法都是包装类，body为返回类型对象
        return body;
    }

    /**
     * 使用URI传参，测试结果会显示在服务提供者的终端中
     * ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType)
     */
    @PostMapping("/entity/uri")
    public User postForEntityByURI(){
        User user = new User("老张","1","678912345");
        //这里只是将url转成URI，并没有添加参数
        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://eureka-service/user")
                                                                                .build().encode();
        URI toUri = uriComponents.toUri();
        //使用user传参
        User object = restTemplate.postForObject(toUri, user, User.class);
        return object;
    }

    /**
     * 这里测试postForObject方法，需要注意的参数如上述方法的描述，区别只是不需要getBody了，这里就不再累述了
     * postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
     * postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
     */
    @PostMapping("/object")
    public User postForObject(){
        User user = new User("hellxz2","1","123654987");
        //这里url传1是为了调用服务者项目中的一个接口
        User responseBody = restTemplate.postForObject("http://eureka-service/user/1", user, User.class);
        return responseBody;
    }

    /**
     * post请求还有一种：postForLocation，这里也同样有三种重载，除了无需指定返回类型外，用法相同，返回类型均为URI，也就不累述了
     * postForLocation(String url, Object request, Object... uriVariables)
     * postForLocation(String url, Object request, Map<String, ?> uriVariables)
     * postForLocation(URI url, Object request)
     */
    @PostMapping("/location")
    public URI postForLocation(){
        User user = new User("hellxz3","1","987654321");
        URI uri = restTemplate.postForLocation("http://eureka-service/location", user);
        //不知道为什么返回来是空，这个方法仅供参考吧，如果知道是什么情况，我会回来改的
        logger.info("/location uri:"+uri);
        return uri;
    }

}
