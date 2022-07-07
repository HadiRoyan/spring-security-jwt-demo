package com.hadroy.authapp.error;

public class UserRegisterException extends RuntimeException{

    private String message;

    public UserRegisterException() {
    }

    public UserRegisterException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
