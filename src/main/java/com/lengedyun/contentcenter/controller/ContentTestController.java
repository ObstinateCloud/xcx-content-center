package com.lengedyun.contentcenter.controller;

import com.lengedyun.contentcenter.domain.dto.user.UserDto;
import com.lengedyun.contentcenter.feign.TestBaiduFeignClient;
import com.lengedyun.contentcenter.feign.TestUserCenterFeignClient;
import com.lengedyun.contentcenter.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: TestController
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/10/8 21:28
 */
@RestController
@RequestMapping("contentTest")
@RefreshScope //nacos管理的配置属性动态刷新
public class ContentTestController {

    @Autowired
    TestUserCenterFeignClient userCenterFeignClient;

    @Autowired
    TestBaiduFeignClient testBaiduFeignClient;

    @Autowired
    TestService testService;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("getReqMuiltArgs")
    public UserDto query(UserDto userDto) {
        return userCenterFeignClient.getReqMuiltArgs(userDto);
    }

    @GetMapping("reqBaidu")
    public String index() {
        return testBaiduFeignClient.index();
    }

    @GetMapping("test-a")
    public String testA() {
        return testService.common() + " test-a";
    }

    @GetMapping("test-b")
    public String testB() {
        return testService.common() + " test-b";
    }

    /*
     * restTemplate exchange方式实现toekn传递
     * */
    @GetMapping("tokenRelay/{userId}")
    public ResponseEntity<UserDto> query(@PathVariable Integer userId, HttpServletRequest request) {
        String token = request.getHeader("X-token");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-token", token);
        return this.restTemplate.exchange(
                "http://user-center/users/{userId}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserDto.class,
                userId
        );
    }

    /*
     * restTemplate interceptor方式实现toekn传递
     * */
    @GetMapping("tokenRelayInterceptor/{userId}")
    public ResponseEntity<UserDto> tokenRelayInterceptor(@PathVariable Integer userId) {
        return this.restTemplate.getForEntity(
                "http://user-center/users/{userId}",
                UserDto.class,
                userId
        );
    }

    @Value("${your.config}")
    private String yourConfig;

    @GetMapping("getNacosConfig")
    public String nacosConfig() {

        return yourConfig;
    }
}
