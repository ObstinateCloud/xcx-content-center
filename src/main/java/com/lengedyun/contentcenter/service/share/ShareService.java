package com.lengedyun.contentcenter.service.share;

import com.lengedyun.contentcenter.dao.share.ShareMapper;
import com.lengedyun.contentcenter.domain.dto.ShareAuditDto;
import com.lengedyun.contentcenter.domain.dto.content.ShareDto;
import com.lengedyun.contentcenter.domain.dto.messaging.UserAddBonusMsgDto;
import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import com.lengedyun.contentcenter.domain.entity.Share;
import com.lengedyun.contentcenter.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @title: ShareService
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/24 7:19
 */
@Service
@Slf4j
public class ShareService {

    @Autowired
    ShareMapper shareMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public ShareDto findShareById(Integer id){
        Share share = shareMapper.selectByPrimaryKey(id);

        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");

        //version1 写死
//        UserDto userDto = restTemplate.getForObject(
//                "http://localhost:8070/users/{id}",
//                UserDto.class,
//                id
//        );
        //version2 discoveryClient优化
//        String targetUrl = instances.stream()
//                .map(instance->instance.getUri().toString()+"/users/{id}")
//                .findFirst()
//                .orElseThrow(()-> new IllegalArgumentException("当前吴实例可用"));
//        log.info("targrtUrl:"+targetUrl);

        //version3 客户端负载均衡手动实现
//        List<String> targetUrls = instances.stream()
//                .map(instance->instance.getUri().toString()+"/users/{id}")
//                .collect(Collectors.toList());
//        int i = ThreadLocalRandom.current().nextInt(targetUrls.size());
//        System.out.println(i);
//        log.info("targrtUrl:"+targetUrls.get(i));

        //version4 采用ribbon实现
//        UserDto userDto = restTemplate.getForObject(
//                "http://user-center//users/{id}",
//                UserDto.class,
//                id
//        );

        //version5 采用feign
        UserDto userDto = userCenterFeignClient.findById(id);

        ShareDto shareDto = new ShareDto();
        //使用spring bean拷贝
        BeanUtils.copyProperties(share,shareDto);
        shareDto.setWxNickname(userDto.getWxNickname());
        return shareDto;
    }

    public Share shareAudit(Integer id, ShareAuditDto shareAuditDto) {
        Share share = shareMapper.selectByPrimaryKey(id);
        if(share == null){
            throw new IllegalArgumentException("参数非法，分享不存在");
        }
        if(Objects.equals("NOT_YET",shareAuditDto.getAuditStatusEnum().toString())){
            throw new IllegalArgumentException("参数非法，该分享无法审核");
        }
        share.setAuditStatus(shareAuditDto.getAuditStatusEnum().toString());
        share.setReason(shareAuditDto.getReason());
        this.shareMapper.updateByPrimaryKey(share);

        //给发布人加积分
        this.rocketMQTemplate.convertAndSend("add-bonus",
                UserAddBonusMsgDto.builder()
                        .userId(share.getUserId())
                        .bonus(50)
                        .build()
        );
        return share;


    }
}
