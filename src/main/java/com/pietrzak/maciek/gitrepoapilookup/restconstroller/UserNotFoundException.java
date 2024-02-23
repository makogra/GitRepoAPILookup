package com.pietrzak.maciek.gitrepoapilookup.restconstroller;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
