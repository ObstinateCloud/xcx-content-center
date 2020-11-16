package com.lengedyun.contentcenter.auth;

import com.lengedyun.contentcenter.security.TokenSecurityException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @title: GlobalExceptionErrorHandler
 * @description: 采用spring全局异常处理 修改返回状态码
 * @auther: 张健云
 * @date: 2020/11/6 8:12
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionErrorHandler {
    /*
    * 采用ResponseEntity 重载返回值类型
    * */
    @ExceptionHandler(TokenSecurityException.class)
    public ResponseEntity<ErrorBody> error(TokenSecurityException e){
         //版本1
//        ResponseEntity<String> responseEntity = new ResponseEntity<String>("token不合法,禁止访问", HttpStatus.UNAUTHORIZED);
        log.warn("发生SecurityException异常",e);
         //版本2
        ResponseEntity<ErrorBody> responseEntity = new ResponseEntity<ErrorBody>(
                ErrorBody.builder()
                        .body(e.getMessage())
                        .errorCode(HttpStatus.UNAUTHORIZED.value())
                        .build()
                , HttpStatus.UNAUTHORIZED);
        return responseEntity;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ErrorBody{

    private String body;

    private int errorCode;
}


