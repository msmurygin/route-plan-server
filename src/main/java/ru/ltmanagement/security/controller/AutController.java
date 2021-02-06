package ru.ltmanagement.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.common.service.infor.InforClientService;
import ru.ltmanagement.security.controller.response.AuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class AutController {

    @Autowired
    private InforClientService authService;
    private static final String SET_COOKIE_HEADER = "Set-Cookie";
    private static final String TOKEN_COOKIE_TEMPLATE = AuthenticationParam.TOKEN.getCode() + "=%s;Path=/; Secure; HttpOnly; domain=localhost; SameSite=None; Path=/; Max-Age=99999999;";

    @GetMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@RequestHeader Map<String, String> headers, HttpServletResponse response){
        AuthResponse generatedToken = authenticateByHeaders(headers);
        //response.addHeader(SET_COOKIE_HEADER, createTokenCookieValue(generatedToken.getToken()));
        return ResponseEntity.status(HttpStatus.OK).body(generatedToken);
    }
    protected AuthResponse authenticateByHeaders(Map<String, String> headers) {
        String user = headers.get(AuthenticationParam.USERNAME.getCode());
        String password = headers.get(AuthenticationParam.PASSWORD.getCode());
        Assert.notNull(user,"User header can not be null");
        Assert.notNull(password,"password header can not be null");
        return authService.login(user, password);
    }
    private String createTokenCookieValue(String token) {
        return String.format(TOKEN_COOKIE_TEMPLATE, token);
    }
}
