package com.lengedyun.contentcenter.controller;

import com.github.pagehelper.PageInfo;
import com.lengedyun.contentcenter.auth.CheckLogin;
import com.lengedyun.contentcenter.domain.dto.content.ShareDto;
import com.lengedyun.contentcenter.domain.entity.Share;
import com.lengedyun.contentcenter.service.share.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ShareDto findById(@PathVariable Integer id) {
        return shareService.findShareById(id);
    }

    /**
     * 分页查询配置列表
     *
     * @return
     */
    @GetMapping("q")
    public PageInfo<Share> q(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        //pagesize 做控制
        if (pageSize > 100) {
            pageSize = 100;
        }

        return this.shareService.q(title,pageNum,pageSize);
    }

}
