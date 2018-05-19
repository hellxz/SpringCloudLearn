package com.cnblogs.hellxz.hystrix;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.servcie.RibbonService;
import com.netflix.hystrix.HystrixCommand;

import java.util.List;

import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;

/**
 * @Author : Hellxz
 * @Description: 批量请求命令的实现
 * @Date : 2018/5/5 11:18
 */
public class UserBatchCommand extends HystrixCommand<List<User>> {

    private RibbonService service;
    /**
     * 这个ids是UserCollapseCommand获取的参数集
     */
    private List<Long> ids;

    public UserBatchCommand(RibbonService ribbonService, List<Long> ids){
        super(Setter.withGroupKey(asKey("userBatch")));
        this.service = ribbonService;
        this.ids = ids;
    }

    @Override
    protected List<User> run() {
        return service.findAll(ids);
    }

}
