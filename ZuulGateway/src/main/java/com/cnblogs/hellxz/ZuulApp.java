package com.cnblogs.hellxz;

        import com.cnblogs.hellxz.filter.AccessFilter;
        import org.springframework.boot.SpringApplication;
        import org.springframework.cloud.client.SpringCloudApplication;
        import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
        import org.springframework.context.annotation.Bean;

@EnableZuulProxy //开启zuul网关服务功能
@SpringCloudApplication
public class ZuulApp {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApp.class, args);
    }

//    @Bean
//    public AccessFilter accessFilter(){
//        return new AccessFilter();
//    }
}
