package com.cnblogs.hellxz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableBinding(value = {Source.class}) //开启绑定功能
@SpringBootTest //测试
public class TestSendMessage {

    @Autowired
    private Source source; //注入消息通道

    @Test
    public void testSender() {
        //向管道发送消息
        source.output().send(MessageBuilder.withPayload("Message from MyPipe").build());
    }
}