package com.lengedyun.contentcenter;

import org.springframework.web.client.RestTemplate;

/**
 * @title: SentinelTest
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/10/9 21:03
 */
public class SentinelTest {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 1000; i++) {
            String forObject = restTemplate.getForObject("http://localhost:8090/actuator/sentinel", String.class);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
