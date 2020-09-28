package com.lengedyun.contentcenter.config;

import com.lengedyun.ribbonconfig.RibbonConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * @title: UserRibbonConfig
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/28 21:21
 */
@Configuration
@RibbonClient(name = "user-center",configuration = RibbonConfig.class)
public class UserRibbonConfig {
}
