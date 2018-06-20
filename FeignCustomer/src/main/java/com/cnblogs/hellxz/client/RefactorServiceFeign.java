package com.cnblogs.hellxz.client;

import com.cnblogs.hellxz.entity.User;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <b>类名</b>: RefactorServiceFeign
 * <p><b>描    述</b>: 用来测试Feign的继承特性，我们可以通过实现要调用的服务提供者的方法，来改变服务返回的结果</p>
 *
 * <p><b>创建日期</b>6/20/18 10:54 PM</p>
 * @author HELLXZ 张
 * @version 1.0
 * @since jdk 1.8
 */
@RestController //必须加上这样的注解让SpringMVC容器读取到，否则会报错找不到这个类
public class RefactorServiceFeign implements EurekaServiceFeign{

    /**
     * 这里只用这个方法来演示，也可以通过将服务提供者的pom当做信赖进行继承，效果是一样的
     */
    @Override
    public String helloFeign() {
        return "refactor succeed!";
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
}
