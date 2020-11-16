package com.lengedyun.contentcenter.auth;

import com.lengedyun.contentcenter.security.TokenSecurityException;
import com.lengedyun.contentcenter.utils.JwtOperator;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @title: AuthAspect
 * @description: 登录状态检查,及权限检查
 * @auther: 张健云
 * @date: 2020/11/6 7:29
 */

@Component
@Aspect
public class AuthAspect {

    @Autowired
    private JwtOperator jwtOperator;

    @Around("@annotation(com.lengedyun.contentcenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        checkToken();
        return point.proceed();//此处的异常应该抛出去，否则会覆盖掉其他内部异常
//        try {
//
//        } catch (Throwable throwable) {
//            throw new TokenSecurityException("token不合法");
//        }


    }

    private void checkToken() {
        //1 从header中获取token
        //spring提供的静态工具类
        HttpServletRequest request = getHttpServletRequest();

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
        request.setAttribute("role", claimsFromToken.get("role"));
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //ServletRequestAttributes是RequestAttributes的实现
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

        return servletRequestAttributes.getRequest();
    }

    @Around("@annotation(com.lengedyun.contentcenter.auth.CheckAuthorization)")
    public Object checkAuth(ProceedingJoinPoint point) throws Throwable {
        //1.检查用户是否登录
        checkToken();
        //2.检查角色是否匹配
        HttpServletRequest request = getHttpServletRequest();
        String role = (String) request.getAttribute("role");
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);
        String value = annotation.value();
//        try {
            if(!Objects.equals(role,value)){
                throw new SecurityException("用户无权访问");
            }
            return point.proceed();//此处的异常应该抛出去，否则会覆盖掉其他内部异常
//        } catch (Throwable throwable) {
//            throw new TokenSecurityException("用户无权访问");
//        }


    }
}
