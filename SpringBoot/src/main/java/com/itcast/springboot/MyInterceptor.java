package com.itcast.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author : Hellxz
 * @Description:自定义拦截器
 * @Date : 2018/4/8 12:36
 */
@Configuration
public class MyInterceptor extends WebMvcConfigurerAdapter {
    /**
     * @Author: Hellxz
     * @Description: 添加自定义拦截器
     * @Date: 2018/4/8 12:39
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 自定义handlerInterceptor,匿名内部类
         */
        HandlerInterceptor handlerInterceptor = new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
                System.out.println("—————进入拦截器");
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
            }

            @Override
            public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
            }
        };
        //将这个handler添加到签名认证拦截器中
        registry.addInterceptor(handlerInterceptor);
//        添加拦截uri
//        registry.addInterceptor(handlerInterceptor).addPathPatterns("/user/**");
        super.addInterceptors(registry);
    }

}
