import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootConfiguration //这里没有使用SpringBootApplication或SpringCloudApplication注解，会报错
@EnableAutoConfiguration
public class ConfigServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApp.class, args);
    }
}
