package com.cnblogs.hellxz.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * <b>类名</b>: HystrixRequestContextServletFilter
 * <p><b>描    述</b>: 实现Filter用于初始化Hystrix请求上下文环境</p>
 *
 * <p><b>创建日期</b>2018/5/18 16:13</p>
 * @author HELLXZ 张
 * @version 1.0
 * @since jdk 1.8
 */
@WebFilter(filterName = "hystrixRequestContextServletFilter",urlPatterns = "/*",asyncSupported = true)
public class HystrixRequestContextServletFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException {
        //初始化Hystrix请求上下文
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            //请求正常通过
            chain.doFilter(request, response);
        } finally {
            //关闭Hystrix请求上下文
            context.shutdown();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}