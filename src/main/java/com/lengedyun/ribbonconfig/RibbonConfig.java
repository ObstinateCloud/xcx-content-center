package com.lengedyun.ribbonconfig;

import com.lengedyun.contentcenter.config.NacosMetaDataVersionControlRule;
import com.lengedyun.contentcenter.config.NacosSameClusterWeightedRule;
import com.lengedyun.contentcenter.config.NacosWeightedRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: RibbonConfig
 * @description: 为某个模块指定的ribbon规则要放在application 平级目录，防止被扫描到，导致规则全局被应用和其他未知问题
 * 父子上下文重叠
 * @auther: 张健云
 * @date: 2020/9/28 21:23
 */
@Configuration
public class RibbonConfig {

//    @Bean
//    public IRule ribbonRule(){
//        return new NacosWeightedRule();
//    }

//    @Bean
//    public IRule ribbonRule(){
//        return new NacosSameClusterWeightedRule();
//    }

    @Bean
    public IRule ribbonRule(){
        return new NacosMetaDataVersionControlRule();
    }

    @Bean
    public IPing ping(){
        return new PingUrl();
    }


}
