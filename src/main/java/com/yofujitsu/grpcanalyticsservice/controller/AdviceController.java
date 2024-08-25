package com.yofujitsu.grpcanalyticsservice.controller;

import com.yofujitsu.grpcanalyticsservice.exception.HeroNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(HeroNotFoundException.class)
    public String heroNotFound(HeroNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    public String server(Exception e) {
        e.printStackTrace();
        return "Server Error";
    }
}
