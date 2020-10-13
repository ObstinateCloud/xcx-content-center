package com.lengedyun.contentcenter.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @title: TestService
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/10/9 21:13
 */

@Service
@Slf4j
public class TestService {

    @SentinelResource("common")
    public String common(){
        log.info("common is invoke");
        return "common";
    }
}
