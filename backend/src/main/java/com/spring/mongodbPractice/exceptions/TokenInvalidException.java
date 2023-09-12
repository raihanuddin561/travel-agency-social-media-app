package com.spring.mongodbPractice.exceptions;

public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException(String message){
        super(message);
    }
}
