package com.lengedyun.contentcenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: UserAddBonusMsgDto
 * @description: 为用户添加积分
 * @auther: 张健云
 * @date: 2020/10/15 8:06
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddBonusDto {

    private Integer userId;

    private Integer bonus;

    private String description;

    private String event;
}
