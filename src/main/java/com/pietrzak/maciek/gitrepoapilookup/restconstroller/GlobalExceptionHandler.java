package com.pietrzak.maciek.gitrepoapilookup.restconstroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> requestError(Exception exc) {
        return new ResponseEntity<>(new UserErrorResponse(404, "Page/API not found"), HttpStatus.NOT_FOUND);
    }
}
