package com.lengedyun.contentcenter.resttemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @title: TestRestTemplateTokenRelayInterceptor
 * @description: RestTemplate 传参方式2
 * @auther: 张健云
 * @date: 2020/11/11 8:08
 */
public class TestRestTemplateTokenRelayInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        //spring提供的静态工具类
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //ServletRequestAttributes是RequestAttributes的实现
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

        HttpServletRequest request = servletRequestAttributes.getRequest();

        String token = request.getHeader("X-token");

//        2.将token传给 RestTemplate
        HttpHeaders headers = httpRequest.getHeaders();
        headers.add("X-token",token);
        //保证请求继续执行
        return clientHttpRequestExecution.execute(httpRequest,bytes);
    }
}
