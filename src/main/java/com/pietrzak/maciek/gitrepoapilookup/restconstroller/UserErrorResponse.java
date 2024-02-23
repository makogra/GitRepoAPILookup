package com.pietrzak.maciek.gitrepoapilookup.restconstroller;

public class UserErrorResponse {

    int Status;
    String message;

    public UserErrorResponse() {
    }

    public UserErrorResponse(int Status, String message) {
        this.Status = Status;
        this.message = message;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
