package com.lengedyun.contentcenter.domain.dto;

import com.lengedyun.contentcenter.domain.enums.AuditStatusEnum;
import lombok.Data;

/**
 * @title: ShareAuditDto
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/10/13 21:43
 */

@Data
public class ShareAuditDto {

    AuditStatusEnum auditStatusEnum;

    String reason;
}
