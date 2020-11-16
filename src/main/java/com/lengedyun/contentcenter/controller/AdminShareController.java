package com.lengedyun.contentcenter.controller;

import com.lengedyun.contentcenter.auth.CheckAuthorization;
import com.lengedyun.contentcenter.domain.dto.ShareAuditDto;
import com.lengedyun.contentcenter.domain.entity.Share;
import com.lengedyun.contentcenter.service.share.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @title: AdminShareController
 * @description: 管理员分享
 * @auther: 张健云
 * @date: 2020/10/13 21:41
 */

@RestController
@Slf4j
@RequestMapping("admin/shares")
public class AdminShareController {

    @Autowired
    ShareService shareService;

    @PutMapping("audit/{id}")
    @CheckAuthorization("admin")//admin角色可以访问
    public Share shareAudit(@PathVariable Integer id, @RequestBody ShareAuditDto shareAuditDto){
        return shareService.shareAudit(id,shareAuditDto);
    }
}
