package com.lengedyun.contentcenter.service.share;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lengedyun.contentcenter.dao.log.RocketmqTransactionLogMapper;
import com.lengedyun.contentcenter.dao.share.ShareMapper;
import com.lengedyun.contentcenter.domain.dto.ShareAuditDto;
import com.lengedyun.contentcenter.domain.dto.content.ShareDto;
import com.lengedyun.contentcenter.domain.dto.messaging.UserAddBonusMsgDto;
import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import com.lengedyun.contentcenter.domain.entity.RocketmqTransactionLog;
import com.lengedyun.contentcenter.domain.entity.Share;
import com.lengedyun.contentcenter.domain.enums.AuditStatusEnum;
import com.lengedyun.contentcenter.feign.UserCenterFeignClient;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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

    @Autowired
    RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    public ShareDto findShareById(Integer id) {
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
        BeanUtils.copyProperties(share, shareDto);
        shareDto.setWxNickname(userDto.getWxNickname());
        return shareDto;
    }

    public Share shareAudit(Integer id, ShareAuditDto shareAuditDto) {
        Share share = shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法，分享不存在");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法，该分享无法审核");
        }

//        if(AuditStatusEnum.PASS.equals(shareAuditDto.getAuditStatusEnum())){
//
//            //给发布人加积分
////        //方式1 普通消息
////        this.rocketMQTemplate.convertAndSend("add-bonus",
////                UserAddBonusMsgDto.builder()
////                        .userId(share.getUserId())
////                        .bonus(50)
////                        .build()
////        );
//            String transactionId = UUID.randomUUID().toString();
//            //方式2 事务消息
//            this.rocketMQTemplate.sendMessageInTransaction(
//                    "add-bonus",
//                    MessageBuilder.withPayload(
//                            UserAddBonusMsgDto.builder()
//                            .userId(share.getUserId())
//                            .bonus(50)
//                            .build()
//                    )
//                    .setHeader(RocketMQHeaders.TRANSACTION_ID,transactionId)
//                    .setHeader("share_id",id)
//                    .build(),
//                    shareAuditDto
//                    );
//
//        }else {
//            this.auditShareByIdInDB(id,shareAuditDto);
//        }
        int i = this.auditShareByIdInDB(id, shareAuditDto);
        if (i == 1) {
            return shareMapper.selectByPrimaryKey(id);
        }
        return share;
    }

    @Transactional(rollbackFor = Exception.class)
    public int auditShareByIdInDB(Integer id, ShareAuditDto shareAuditDto) {

        Share build = Share.builder()
                .id(id)
                .auditStatus(shareAuditDto.getAuditStatusEnum().toString())
                .reason(shareAuditDto.getReason())
                .updateTime(new Date())
                .build();

        return this.shareMapper.updateByPrimaryKeySelective(build);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditShareByIdInDBWithLog(Integer id, ShareAuditDto shareAuditDto, String transactionId) {

        this.auditShareByIdInDB(id, shareAuditDto);

        this.rocketmqTransactionLogMapper.insert(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .logContent("分享审核")
                        .build()
        );

        System.out.println("执行完成");

    }

    public PageInfo<Share> q(String title, Integer pageNum, Integer pageSize) {
//        1.PageHelper 切入sql语句加上分页sql
        PageHelper.startPage(pageNum,pageSize);
//        2.编写不分页sql
        List<Share> shares = this.shareMapper.selectByParam(title);
        return new PageInfo<>(shares);
    }
}
