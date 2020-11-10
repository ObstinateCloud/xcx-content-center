package com.lengedyun.contentcenter.feign;

import com.lengedyun.contentcenter.config.UserCenterFeignConfig;
import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @title: UserCenterFeignClient
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/30 7:40
 */
@FeignClient(value = "user-center",configuration = UserCenterFeignConfig.class)
public interface UserCenterFeignClient {

    //token传递方式1 springmvc RequestHeader注解
    @RequestMapping("/users/{id}")
    UserDto findById(@PathVariable Integer id);
}
