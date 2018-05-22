package com.cnblogs.hellxz.hystrix;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.servcie.RibbonService;
import com.netflix.hystrix.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;

/**
 * @Author : Hellxz
 * @Description: 批量请求命令的实现
 * @Date : 2018/5/5 11:18
 */
public class UserBatchCommand extends HystrixCommand<List<User>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBatchCommand.class);

    private RibbonService service;
    /**
     * 这个ids是UserCollapseCommand获取的参数集
     */
    private List<Long> ids;

    public UserBatchCommand(RibbonService ribbonService, List<Long> ids){
        super(Setter.withGroupKey(asKey("userBatchCommand")));
        this.service = ribbonService;
        this.ids = ids;
    }

    /**
     * <b>方法名</b>: run
     * <p><b>描    述</b>: 调用服务层的简单调用返回集合</p>
     *
     * @param
     * @return  List<User>
     *
     * <p><b>创建日期</b> 2018/5/22 12:39 </p>
     * @author HELLXZ 张
     * @version 1.0
     * @since jdk 1.8
     */
    @Override
    protected List<User> run() {
        List<User> users = service.findAll(ids);
        System.out.println(users);
        return users;
    }

    /**
     * Fallback回调方法，如果没有会报错
     */
    @Override
    protected List<User> getFallback(){
        LOGGER.info("UserBatchCommand的run方法，调用失败");
        return null;
    }

}
