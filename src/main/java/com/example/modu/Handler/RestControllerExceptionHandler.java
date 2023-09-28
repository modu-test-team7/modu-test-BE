package com.example.modu.Handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "Exception Handler")
public class RestControllerExceptionHandler {

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> converstionException(HttpMessageConversionException e)
    {
        return ResponseEntity.badRequest().body("변환도중 에러가 발생하였습니다. 계속 발생시 image값을 Null값으로 보내주세요. 공백문자 불가 \n"
                + e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundEntity(Exception e)
    {
        log.error(e.getMessage(), e);

        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
