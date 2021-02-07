package ru.ltmanagement.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.ltmanagement.common.service.infor.InforClientService;
import ru.ltmanagement.user.dao.UserDao;
import ru.ltmanagement.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomAuthProvider implements AuthenticationProvider{

    @Autowired
    private InforClientService inforClientService;

    @Autowired
    private UserDao userDao;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        Object credentials = authentication.getCredentials();
        System.out.println("credentials class: " + credentials.getClass());
        if (!(credentials instanceof String)) {
            return null;
        }
        String password = credentials.toString();
        inforClientService.login(name, password);
        Optional<UserDto> userOptional = userDao.getUserByUserName(name);
        if (!userOptional.isPresent()) {
            throw new BadCredentialsException("Authentication failed for " + name);
        }

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = userOptional.get().getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.addAll(simpleGrantedAuthorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuthorities);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    private static class User {
        private String name;
        private String password;
        private String role;

        public User(String name, String password, String role) {
            this.name = name;
            this.password = password;
            this.role = role;
        }

        public boolean match(String name, String password) {
            return this.name.equals(name) && this.password.equals(password);
        }
    }
}
