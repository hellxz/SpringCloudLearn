package com.cnblogs.hellxz;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.SubscribableChannel;

public interface MyPipe {

    //方法1
//    @Input(Source.OUTPUT) //Source.OUTPUT的值是output，我们自定义也是一样的
//    SubscribableChannel input(); //使用@Input注解标注的输入管道需要使用SubscribableChannel来订阅通道（主题）

    //========二选一使用===========

    //方法2
    String INPUT = "output";

    @Input(MyPipe.INPUT)
    SubscribableChannel input();
}