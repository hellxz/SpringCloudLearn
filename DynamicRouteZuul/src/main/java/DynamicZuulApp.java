import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableZuulProxy
@SpringCloudApplication
@ComponentScan("com.cnblogs.hellxz")
public class DynamicZuulApp {

    public static void main(String[] args) {
        SpringApplication.run(DynamicZuulApp.class, args);
    }

    @Bean
    @RefreshScope
    @ConfigurationProperties("zuul")
    public ZuulProperties zuulProperties(){
        return new ZuulProperties();
    }
}
