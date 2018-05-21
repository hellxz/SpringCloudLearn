package com.cnblogs.hellxz.test;

import com.cnblogs.hellxz.entity.User;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * <p><b>描    述</b>: </p>
 *
 * <p><b>创建日期</b> 2018/5/21 18:13 </p>
 *
 * @author HELLXZ 张辰光
 * @version 1.0
 * @since jdk 1.8
 */
public class CollapseTest {

    public static void main(String[] args) {
        MyThread thread1 = new MyThread();
        MyThread thread2 = new MyThread();
        MyThread thread3 = new MyThread();
        thread1.run();
        thread2.run();
        thread3.run();
    }


}
class MyThread extends Thread{
    RestTemplate restTemplate = new RestTemplate();
    @Override
    public void run(){
        restTemplate.getForObject("http://localhost:8088/hystrix/users/"+new Random().nextInt(100),User.class);
    }
}
