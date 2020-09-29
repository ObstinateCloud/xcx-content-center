package com.lengedyun.contentcenter.feign;

import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @title: UserCenterFeignClient
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/30 7:40
 */
@FeignClient("user-center")
public interface UserCenterFeignClient {

    @RequestMapping("/users/{id}")
    UserDto findById(@PathVariable Integer id);
}
