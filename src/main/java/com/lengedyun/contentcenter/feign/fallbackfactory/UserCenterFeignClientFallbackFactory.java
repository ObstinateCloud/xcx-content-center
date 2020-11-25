package com.lengedyun.contentcenter.feign.fallbackfactory;

import com.lengedyun.contentcenter.domain.dto.user.UserAddBonusDto;
import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import com.lengedyun.contentcenter.feign.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @title: UserCenterFeignClientFallbackFactory
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/11/25 7:22
 */
@Component
@Slf4j
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {
    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        return new UserCenterFeignClient() {
            @Override
            public UserDto findById(Integer id) {
                log.warn("远程调用被限流/降级了");
                UserDto userDTO = new UserDto();
                userDTO.setWxNickname("流控/降级返回的用户");
                return userDTO;
            }

            @Override
            public Integer addUserBonus(UserAddBonusDto userAddBonseDTO) {
                log.warn("添加积分失败");
                return null;
            }
        };
    }
}
