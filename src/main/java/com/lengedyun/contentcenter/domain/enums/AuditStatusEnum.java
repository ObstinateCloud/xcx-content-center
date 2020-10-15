package com.lengedyun.contentcenter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @title: AuditStatusEnum
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/10/13 21:44
 */

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {

    NOT_YET,//待审核
    PASS,
    REJECT


}
