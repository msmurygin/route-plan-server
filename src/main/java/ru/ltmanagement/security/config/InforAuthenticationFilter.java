package ru.ltmanagement.security.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ltmanagement.common.service.infor.InforClientService;
import ru.ltmanagement.security.controller.AuthenticationParam;
import ru.ltmanagement.security.service.AuthenticationManagerService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//@Component
public class InforAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(InforAuthenticationFilter.class);
    private static final String SET_COOKIE_HEADER = "Set-Cookie";
    private static final String TOKEN_COOKIE_TEMPLATE = AuthenticationParam.TOKEN.getCode() + "=%s;Path=/";

    @Autowired
    private AuthenticationManagerService authenticationMgr;

    private final InforClientService inforClientService;

    private final Set<String> notCheckedEndpoints;

    public InforAuthenticationFilter(InforClientService inforClientService,
                                     @Value("${application.security.unsecured-methods:/**}")
                                             String[] unsecuredMethods) {
        this.inforClientService = inforClientService;
        this.notCheckedEndpoints = new HashSet<>(Arrays.asList(unsecuredMethods));
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        for (String notCheckedEndpoint : notCheckedEndpoints) {
            if (request.getRequestURI().contains(notCheckedEndpoint)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        String user = request.getHeader("username");
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(token)){
            setUnauthorized(response);
        }
        log.debug("Trying to authenticate with user {} and token {}", user, token);
        //InforAuthentication inforAuthentication = new InforAuthentication(user, token);
        try {
            String generatedToken = authenticationMgr.authenticate(user, token);
            //SecurityContextHolder.getContext().setAuthentication(inforAuthentication);
            //response.addHeader(SET_COOKIE_HEADER, createTokenCookieValue(generatedToken));
            log.debug("Authenticated successfully");
        } catch (Exception e) {
            log.debug("Unsuccessful authentication", e);
            setUnauthorized(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setUnauthorized(HttpServletResponse response) throws IOException {
        //SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private String createTokenCookieValue(String token) {
        return String.format(TOKEN_COOKIE_TEMPLATE, token);
    }
}
