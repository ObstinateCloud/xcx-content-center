package com.lengedyun.contentcenter.controller;

import com.lengedyun.contentcenter.auth.CheckLogin;
import com.lengedyun.contentcenter.domain.dto.content.ShareDto;
import com.lengedyun.contentcenter.domain.entity.Share;
import com.lengedyun.contentcenter.service.share.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @title: TestController
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/23 8:02
 */

@RestController
@RequestMapping("shares")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @GetMapping("/{id}")
    @CheckLogin
    public ShareDto findById(@PathVariable Integer id){
        return shareService.findShareById(id);
    }

}
