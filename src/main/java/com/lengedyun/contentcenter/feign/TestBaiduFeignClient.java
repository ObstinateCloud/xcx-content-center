package com.lengedyun.contentcenter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @title: TestBaiduFeignClient
 * @description: 脱离ribbon使用feign
 * @auther: 张健云
 * @date: 2020/10/8 21:52
 */

@FeignClient(name = "baidu",url = "http://www.baidu.com")//name随便写但不能不写
public interface TestBaiduFeignClient {

    @GetMapping("")//此处不能再随便拼接内容
    String index();
}
