package com.lengedyun.contentcenter.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: TokenRelayRequestInterceptor
 * @description: 采用拦截器实现feign token传递
 * @auther: 张健云
 * @date: 2020/11/10 8:06
 */
public class TokenRelayRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
//        1.获取token
        //spring提供的静态工具类
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //ServletRequestAttributes是RequestAttributes的实现
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

        HttpServletRequest request = servletRequestAttributes.getRequest();

        String token = request.getHeader("X-token");

        //2 传递token
        if (!StringUtils.isEmpty(token)) {
            requestTemplate.header("X-token", token);
        }
    }
}
