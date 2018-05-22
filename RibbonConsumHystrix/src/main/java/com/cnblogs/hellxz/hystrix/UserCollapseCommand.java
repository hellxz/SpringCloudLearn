package com.cnblogs.hellxz.hystrix;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.servcie.RibbonService;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//注意这个asKey方法不是HystrixCommandKey.Factory.asKey
import static com.netflix.hystrix.HystrixCollapserKey.Factory.asKey;


/**
 * @Author : Hellxz
 * @Description: 继承HystrixCollapser的请求合并器
 * @Date : 2018/5/5 11:42
 */
public class UserCollapseCommand extends HystrixCollapser<List<User>,User,Long> {

    private RibbonService service;
    private Long userId;

    /**
     * 构造方法，主要用来设置这个合并器的时间，意为每多少毫秒就会合并一次
     * @param ribbonService 调用的服务
     * @param userId 单个请求传入的参数
     */
    public UserCollapseCommand(RibbonService ribbonService, Long userId){
        super(Setter.withCollapserKey(asKey("userCollapseCommand")).andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
        this.service = ribbonService;
        this.userId = userId;
    }

    /**
     * 获取请求中的参数
     */
    @Override
    public Long getRequestArgument() {
        return userId;
    }

    /**
     * 创建命令，执行批量操作
     */
    @Override
    public HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        //按请求数声名UserId的集合
        List<Long> userIds = new ArrayList<>(collapsedRequests.size());
        //通过请求将100毫秒中的请求参数取出来装进集合中
        userIds.addAll(collapsedRequests.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        //返回UserBatchCommand对象，自动执行UserBatchCommand的run方法
        return new UserBatchCommand(service, userIds);
    }

    /**
     * 将返回的结果匹配回请求中
     * @param batchResponse 批量操作的结果
     * @param collapsedRequests 合在一起的请求
     */
    @Override
    protected void mapResponseToRequests(List<User> batchResponse, Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        int count = 0 ;
        for(CollapsedRequest<User,Long> collapsedRequest : collapsedRequests){
            //从批响应集合中按顺序取出结果
            User user = batchResponse.get(count++);
            //将结果放回原Request的响应体内
            collapsedRequest.setResponse(user);
        }
    }
}
