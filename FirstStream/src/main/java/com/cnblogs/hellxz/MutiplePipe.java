package com.cnblogs.hellxz;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 多个输出管道
 */
public interface MutiplePipe {

    @Output("output1")
    MessageChannel output1();

    @Output("output2")
    MessageChannel output2();
}
