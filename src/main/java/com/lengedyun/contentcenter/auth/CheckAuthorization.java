package com.lengedyun.contentcenter.auth;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @title: CheckAuthorization
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/11/16 6:35
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {

    String value();
}
