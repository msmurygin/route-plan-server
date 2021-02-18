package ru.ltmanagement.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.security.controller.request.LoginRequest;
import ru.ltmanagement.security.controller.response.LoginResponse;
import ru.ltmanagement.security.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class SecurityController {

    private static final String AUTH_HEADER = "Authorization";


    @Autowired
    protected LoginService loginService;

    @CrossOrigin(origins = {"http://localhost:4200", "http://sekbtt-wmsapp01.myway.local:4200/"}, methods = { RequestMethod.GET, RequestMethod.OPTIONS})
    @GetMapping("/public/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }


    @CrossOrigin(origins = {"http://localhost:4200", "http://sekbtt-wmsapp01.myway.local:4200/" ,"http://sekbtt-wmsapp01.myway.local/"}, methods = { RequestMethod.GET, RequestMethod.OPTIONS})
    @GetMapping("/public/login")
    public ResponseEntity<LoginResponse> login(HttpServletRequest request) {
        String authorization  = request.getHeader(AUTH_HEADER);
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            LoginResponse login = loginService.login(new LoginRequest(values[0],  values[1]));
            return ResponseEntity.ok(login);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
