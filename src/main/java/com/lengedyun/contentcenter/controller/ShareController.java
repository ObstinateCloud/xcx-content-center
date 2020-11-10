package com.lengedyun.contentcenter.controller;

import com.lengedyun.contentcenter.auth.CheckLogin;
import com.lengedyun.contentcenter.domain.dto.content.ShareDto;
import com.lengedyun.contentcenter.domain.entity.Share;
import com.lengedyun.contentcenter.service.share.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
