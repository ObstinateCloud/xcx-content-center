package com.lengedyun.contentcenter.security;

/**
 * @title: SecurityException
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/11/6 7:40
 */
public class TokenSecurityException extends RuntimeException{

    public TokenSecurityException(String msg) {
        super(msg);
    }
}
