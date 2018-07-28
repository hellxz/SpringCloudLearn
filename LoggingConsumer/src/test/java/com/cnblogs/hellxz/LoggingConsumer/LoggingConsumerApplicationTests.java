package com.cnblogs.hellxz.LoggingConsumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LoggingConsumerApplicationTests {

	@Autowired
	private Sink sink;

	@Test
	public void contextLoads() {
		assertNotNull(this.sink.input());
	}
}
