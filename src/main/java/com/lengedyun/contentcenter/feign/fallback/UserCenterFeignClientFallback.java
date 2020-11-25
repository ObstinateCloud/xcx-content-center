package com.lengedyun.contentcenter.feign.fallback;

import com.lengedyun.contentcenter.domain.dto.user.UserAddBonusDto;
import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import com.lengedyun.contentcenter.feign.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * @title: UserCenterFeignClientFallback
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/11/25 7:24
 */
@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {

    @Override
    public UserDto findById(Integer id) {
        return null;
    }

    @Override
    public Integer addUserBonus(UserAddBonusDto build) {
        return null;
    }
}
