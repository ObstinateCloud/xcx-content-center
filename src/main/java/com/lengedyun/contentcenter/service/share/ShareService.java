package com.lengedyun.contentcenter.service.share;

import com.lengedyun.contentcenter.dao.share.ShareMapper;
import com.lengedyun.contentcenter.domain.dto.content.ShareDto;
import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import com.lengedyun.contentcenter.domain.entity.Share;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @title: ShareService
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/24 7:19
 */
@Service
public class ShareService {

    @Autowired
    ShareMapper shareMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;

    public ShareDto findShareById(Integer id){
        Share share = shareMapper.selectByPrimaryKey(id);
        UserDto userDto = restTemplate.getForObject(
                "http://localhost:8070/users/{id}",
                UserDto.class,
                id
        );
        ShareDto shareDto = new ShareDto();
        //使用spring bean拷贝
        BeanUtils.copyProperties(share,shareDto);
        shareDto.setWxNickname(userDto.getWxNickname());
        return shareDto;
    }
}
