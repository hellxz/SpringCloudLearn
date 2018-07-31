package com.cnblogs.hellxz;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.SubscribableChannel;

public interface MyPipe {

    //方法1
//    @Input(Source.OUTPUT)
//    SubscribableChannel input();

    //========二选一使用===========

    //方法2
    String INPUT = "output";

    @Input(MyPipe.INPUT)
    SubscribableChannel input();
}