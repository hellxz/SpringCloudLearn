package com.cnblogs.hellxz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * 第一个Spring Cloud Steam 应用
 */
@SpringBootApplication
@EnableBinding({Sink.class, MyPipe.class}) //开启消息绑定功能
public class FirstStreamApp {

	private static Logger logger = LoggerFactory.getLogger(FirstStreamApp.class);

	public static void main(String[] args) {
		SpringApplication.run(FirstStreamApp.class, args);
	}

	/**
	 * 监听以Sink.INPUT的值input名称的管道同名的消息通道，输出消息
	 * @param payload 消息体，支持自动转型，如果使用网页端发送可能会输出对象名@hash值
	 */
	@StreamListener(Sink.INPUT)
	public void receive(Object payload) {
		logger.info("Received: " + payload);
	}

	/**
	 * 接收自定义MyPipe.INPUT值为output的通道的消息，输出消息
	 * @param payload 消息体
	 */
	@StreamListener(MyPipe.INPUT)
	public void receiveFromMyPipe(Object payload){
		logger.info("Received: "+payload);
	}
}