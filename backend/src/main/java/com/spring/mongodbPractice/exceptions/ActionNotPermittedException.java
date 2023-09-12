package com.spring.mongodbPractice.exceptions;

public class ActionNotPermittedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ActionNotPermittedException(String message) {
        super(message);
    }
}
