package com.lengedyun.contentcenter.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @title: NacosMetaDataVersionControlRule
 * @description:  扩展riboon基于nacos元数据的版本控制
 * @auther: 张健云
 * @date: 2020/9/29 7:32
 */
@Slf4j
public class NacosMetaDataVersionControlRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired
    private DiscoveryClient discoveryClient;


    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        //1.获取当前实例的元数据信息
        Map<String, String> currMetadata = nacosDiscoveryProperties.getMetadata();

        //2.获取可用目标实例列表
        BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        String targetName = loadBalancer.getName();
        //拿到所有的健康实例
        List<Instance> instances = null;
        try {
            instances = nacosDiscoveryProperties.namingServiceInstance().selectInstances(targetName, true);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        //3.筛选出符合当前实例的目标版本的实例列表
        List<Instance> suitCollect = instances.stream().filter(instance -> Objects.equals(instance.getMetadata().get("version"), currMetadata.get("target-version"))).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(suitCollect)){
            log.error("没有符合条件的实例name={}",targetName);
            return null;
        }else{
            //4.选择一个健康的实例
            Instance hostByRandomWeightMy = ExtendBalancerMetaDate.getHostByRandomWeightMy(suitCollect);
            log.info("请求的实例为port={}",hostByRandomWeightMy.getPort());
            return new NacosServer(hostByRandomWeightMy);
        }
    }
}
//getHostByRandomWeight 符合调用需求但是，protected方法无法直接使用所以集成他的类使用
class ExtendBalancerMetaDate extends Balancer {

    public static Instance getHostByRandomWeightMy(List<Instance> instances){
        return  getHostByRandomWeight(instances);
    }
}