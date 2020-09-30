package com.lengedyun.contentcenter.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: UserCenterFeignConfig
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/30 8:09
 *
 */
//@Configuration //此处不能加configuration 否则会被全局共享，解决方式：同ribbon 移动到application扫描不到的地方
public class UserCenterFeignConfig {

    @Bean
    public Logger.Level logLevel(){
        return Logger.Level.FULL;
    }
}
