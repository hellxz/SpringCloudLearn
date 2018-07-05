package com.cnblogs.hellxz.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问过滤器
 */
@Component
public class AccessFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    /**
     * 过滤器类型选择：
     * pre 为路由前
     * route 为路由过程中
     * post 为路由过程后
     * error 为出现错误的时候
     * 同时也支持static ，返回静态的响应，详情见StaticResponseFilter的实现
     * 以上类型在会创建或添加或运行在FilterProcessor.runFilters(type)
     */
    @Override
    public String filterType() {
        return "pre"; //ZuulFilter源码中注释"pre"为在路由前过滤
    }

    /**
     * 用来过滤器排序执行的
     * @return 排序的序号
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否通过这个过滤器，默认为true，改成false则不启用
     */
    @Override
    public boolean shouldFilter() {
        return true; //返回true表示执行这个过滤器
    }

    /**
     * 过滤器的逻辑
     */
    @Override
    public Object run() {
        //获取当前请求上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //取出当前请求
        HttpServletRequest request = ctx.getRequest();
        logger.info("进入访问过滤器，访问的url:{}，访问的方法：{}",request.getRequestURL(),request.getMethod());
        //从headers中取出key为accessToken值
        String accessToken = request.getHeader("accessToken");//这里我把token写进headers中了
        //这里简单校验下如果headers中没有这个accessToken或者该值为空的情况
        //那么就拦截不入放行，返回401状态码
        if(StringUtils.isEmpty(accessToken)) {
            logger.info("当前请求没有accessToken");
            //使用Zuul来过滤这次请求
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        }
        logger.info("请求通过过滤器");
        return null;
    }
}
