package com.lengedyun.contentcenter.auth;

import com.lengedyun.contentcenter.security.TokenSecurityException;
import com.lengedyun.contentcenter.utils.JwtOperator;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: CheckLoginAspect
 * @description: 登录状态检查
 * @auther: 张健云
 * @date: 2020/11/6 7:29
 */

@Component
@Aspect
public class CheckLoginAspect {

    @Autowired
    private JwtOperator jwtOperator;

    @Around("@annotation(com.lengedyun.contentcenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) {
        try {
            //1 从header中获取token
            //spring提供的静态工具类
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            //ServletRequestAttributes是RequestAttributes的实现
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

            HttpServletRequest request = servletRequestAttributes.getRequest();

            String token = request.getHeader("X-token");

            //2 判断token是否合法
            Boolean isValidate = jwtOperator.validateToken(token);
            if (!isValidate) {
                throw new TokenSecurityException("token不合法");
            }

            //3 如果合法就将用户信息写到request的attribute中
            Claims claimsFromToken = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id", claimsFromToken.get("id"));
            request.setAttribute("wxNickname", claimsFromToken.get("wxNickname"));
            request.setAttribute("role", claimsFromToken.get("wxNickname"));
            return point.proceed();
        } catch (Throwable throwable) {
            throw new TokenSecurityException("token不合法");
        }


    }
}
