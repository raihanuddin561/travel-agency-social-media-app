package com.spring.mongodbPractice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler {
    @ExceptionHandler(value = {UserServiceException.class, UsernameNotFoundException.class, AuthenticationException.class})
    public ResponseEntity<Object> handleUserServiceException(Exception ex, WebRequest request){
        ErrorMessage error = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(error,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request){
        ErrorMessage error = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(error,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}