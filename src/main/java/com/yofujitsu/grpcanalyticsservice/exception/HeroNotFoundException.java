package com.yofujitsu.grpcanalyticsservice.exception;

public class HeroNotFoundException extends RuntimeException{

    public HeroNotFoundException(String message) {
        super(message);
    }
}
