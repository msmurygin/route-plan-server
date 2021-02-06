package ru.ltmanagement.exceptions;

public class LoginFailedException extends RuntimeException {

    public LoginFailedException(String msg){
        super(msg);
    }
}
