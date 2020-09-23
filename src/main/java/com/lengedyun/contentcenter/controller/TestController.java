package com.lengedyun.contentcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @title: TestController
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/23 8:02
 */

@RestController
public class TestController {

    @GetMapping("sayHello")
    public String sayHello(){
        return "hello zjy";
    }

}
