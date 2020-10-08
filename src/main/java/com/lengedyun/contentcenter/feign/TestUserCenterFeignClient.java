package com.lengedyun.contentcenter.feign;

import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @title: TestUserCenterFeignClient
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/10/8 21:26
 */
@FeignClient(name = "user-center")
public interface TestUserCenterFeignClient {

    @GetMapping("userTest/getReqMuiltArgs")
    UserDto getReqMuiltArgs(@SpringQueryMap UserDto userDto);//feign多参数调用
}
