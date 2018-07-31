package com.cnblogs.hellxz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableBinding({Sink.class, MyPipe.class})
public class FirstStreamApp {

	private static Logger logger = LoggerFactory.getLogger(FirstStreamApp.class);

	public static void main(String[] args) {
		SpringApplication.run(FirstStreamApp.class, args);
	}

	@StreamListener(Sink.INPUT)
	public void receive(Object payload) {
		logger.info("Received: " + payload);
	}

	@StreamListener(MyPipe.INPUT)
	public void receiveFromMyPipe(Object payload){
		logger.info("Received: "+payload);
	}
}