package com.lengedyun.contentcenter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @title: RestTemplateTest
 * @description: TODO
 * @auther: 张健云
 * @date: 2020/9/24 7:31
 */
public class RestTemplateTest {


    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();

        String responseObj = restTemplate.getForObject(
                "http://localhost:8080/users/{id}",
                String.class,
                1
        );
        System.out.println(responseObj);

        //getForEntity 可以获取状态码
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "http://localhost:8080/users/{id}",
                String.class,
                1
        );
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getStatusCodeValue());

    }
}
