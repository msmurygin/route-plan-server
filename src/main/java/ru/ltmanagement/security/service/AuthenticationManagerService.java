package ru.ltmanagement.security.service;

public interface AuthenticationManagerService {
    String authenticate(String login, String token);
}
