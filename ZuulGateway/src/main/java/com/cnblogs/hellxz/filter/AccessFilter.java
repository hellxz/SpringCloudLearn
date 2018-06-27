package com.cnblogs.hellxz.filter;

import com.netflix.zuul.ZuulFilter;

/**
 * 访问过滤器
 */
public class AccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {
        return null;
    }
}
