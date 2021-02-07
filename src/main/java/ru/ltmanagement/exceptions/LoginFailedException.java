package ru.ltmanagement.exceptions;

import org.springframework.security.core.AuthenticationException;

public class LoginFailedException extends AuthenticationException {

    public LoginFailedException(String msg){
        super(msg);
    }
}
