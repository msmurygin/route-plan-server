package ru.ltmanagement.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.exceptions.InvalidTokenException;
import ru.ltmanagement.security.JwtProvider;

@Service
public class AuthenticationManagerServiceImpl implements AuthenticationManagerService {

    @Autowired
    private JwtProvider jwtProvider;

    public String authenticate(String login, String token){
        boolean validateToken = jwtProvider.validateToken(login, token);
        String loginFromToken = jwtProvider.getLoginFromToken(token);
        if (!validateToken || !loginFromToken.equalsIgnoreCase(login)) throw new InvalidTokenException("Invalid token");
        return token;
    }
}
