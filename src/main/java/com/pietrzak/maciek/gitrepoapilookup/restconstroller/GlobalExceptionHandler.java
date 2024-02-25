package com.pietrzak.maciek.gitrepoapilookup.restconstroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<UserErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatusCode status = ex.getStatusCode();
        UserErrorResponse errorResponse = new UserErrorResponse(status.value(), ex.getReason());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<UserErrorResponse> handlePageNotFound(NoResourceFoundException exc){
        UserErrorResponse errorResponse = new UserErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage("Page not found.");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserErrorResponse> handleException(Exception ex) {
        System.out.println(ex.getClass().getName());
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
