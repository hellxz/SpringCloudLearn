package com.cnblogs.hellxz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableBinding(value = {MutiplePipe.class}) //开启绑定功能
@SpringBootTest //测试
public class TestMultipleOutput {

    @Autowired @Qualifier("output1")
    private MessageChannel messageChannel;

    @Test
    public void testSender() {
        //向管道发送消息
        messageChannel.send(MessageBuilder.withPayload("produce by multiple pipe").build());
    }
}
