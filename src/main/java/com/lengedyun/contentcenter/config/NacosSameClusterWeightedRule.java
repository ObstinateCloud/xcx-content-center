package com.lengedyun.contentcenter.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @title: NacosSameClusterWeightedRule
 * @description: 扩展ribbon  基于nacos的同一集群下优先调用
 * @auther: 张健云
 * @date: 2020/9/29 6:34
 */
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        //获取配置文件中本实例的cluster name
        String clusterName = nacosDiscoveryProperties.getClusterName();
        //想要请求的微服务的名称
        BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        String name = loadBalancer.getName();

        //拿到服务发现相关api
        try {
            List<Instance> instances = nacosDiscoveryProperties.namingServiceInstance().selectInstances("user-center", true);
            //过滤出相同集群下所有的实例
            List<Instance> sameClusterInsts = instances.stream().filter(inst -> Objects.equals(inst.getClusterName(), clusterName)).collect(Collectors.toList());
            List<Instance> chosen = new ArrayList<>();
            //如何本集群下没有实例name就其他实例
            if (CollectionUtils.isEmpty(sameClusterInsts)) {
                log.warn("发生跨集群调用name={},clusterName={}", name, clusterName);
                chosen = instances;
            } else {
                chosen = sameClusterInsts;
            }
            //基于权重负载均衡返回一个类，没有现成的方法所以跟踪selectOneHealthyInstance代码实现
            Instance hostByRandomWeightMy = ExtendBalancer.getHostByRandomWeightMy(chosen);
            log.info("选择的实例port={}",hostByRandomWeightMy.getPort());
            return new NacosServer(hostByRandomWeightMy);
        } catch (NacosException e) {
            log.error("调用同集群异常", e);
            e.printStackTrace();
        }

        return null;
    }
}

//getHostByRandomWeight 符合调用需求但是，protected方法无法直接使用所以集成他的类使用
class ExtendBalancer extends Balancer{

    public static Instance getHostByRandomWeightMy(List<Instance> instances){
        return  getHostByRandomWeight(instances);
    }
}