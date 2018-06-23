package com.cnblogs.hellxz.client;

import com.cnblogs.hellxz.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务降级回调类
 */
@Component //必须加这个注解，让这个类做为Bean交给Spring管理，否则会报找不到降级类
public class EurekaServiceFeignFallback implements EurekaServiceFeign{

    /**
     * 这里只用这个方法举例了，返回一个“失败"
     */
    @Override
    public String helloFeign() {
        return "失败";
    }

    @Override
    public String greetFeign(String dd) {
        return null;
    }

    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        return null;
    }

    @Override
    public String getParamByHeaders(String name) {
        return null;
    }

    @Override
    public User getUserByRequestBody(User user) {
        return null;
    }

    @Override
    public String feignRetry() {
        return null;
    }
}
