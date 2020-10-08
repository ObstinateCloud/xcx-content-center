package com.lengedyun.contentcenter.controller;

import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import com.lengedyun.contentcenter.feign.TestUserCenterFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: TestController
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/10/8 21:28
 */
@RestController
@RequestMapping("contentTest")
public class ContentTestController {

    @Autowired
    TestUserCenterFeignClient userCenterFeignClient;

    @GetMapping("getReqMuiltArgs")
    public UserDto query(UserDto userDto){
        return userCenterFeignClient.getReqMuiltArgs(userDto);
    }
}
