package com.spring.mongodbPractice.exceptions;

import com.spring.mongodbPractice.utils.LogUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * this is Exception handler
 *
 * @author raihan
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class AppExceptionsHandler {
    static ErrorMessage errorMessage;
    private final LogUtils logUtils;

    /**
     * setting ErrorMessage
     *
     * @param date
     * @param message
     * @author raihan
     */
    public void setErrorMessge(Date date, String message) {
        errorMessage = ErrorMessage.builder()
                .timeStamp(date)
                .message(message)
                .build();
    }

    /**
     * handler for UsernameNotFoundException, AuthenticationException
     *
     * @param ex
     * @param request
     * @return ResponseEntity<ErrorMessage>
     * @author raihan
     */
    @ExceptionHandler(value = {UserServiceException.class, UsernameNotFoundException.class, AuthenticationException.class})
    public ResponseEntity<ErrorMessage> handleUserServiceException(Exception ex, HttpServletRequest request) {
        String message = ex.getMessage();
        String path = request.getRequestURI();
        setErrorMessge(new Date(), message);
        logUtils.printErrorLog(message, path);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for ActionNotPermittedException
     *
     * @param ex
     * @param request
     * @return ErrorMessage
     * @author raihan
     */
    @ExceptionHandler(ActionNotPermittedException.class)
    public ResponseEntity<ErrorMessage> handleActionNotPermitException(ActionNotPermittedException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        String path = request.getRequestURI();
        setErrorMessge(new Date(), message);
        logUtils.printErrorLog(message, path, request.getUserPrincipal().getName());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ExpiredJwtException.class,TokenInvalidException.class})
    public ResponseEntity<ErrorMessage> handleTokenInvalidException(RuntimeException ex, HttpServletRequest request){
        String message = ex.getMessage();
        String path = request.getRequestURI();
        setErrorMessge(new Date(),message);
        logUtils.printErrorLog(message,path,request.getUserPrincipal().getName());
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }
    /**
     * Handler for Exception
     *
     * @param ex
     * @param request
     * @return Object
     * @author raihan
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
        String message = ex.getMessage();
        String path = request.getRequestURI();
        setErrorMessge(new Date(), message);
        logUtils.printErrorLog(message, path);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


}