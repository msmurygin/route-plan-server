package ru.ltmanagement.security.service;

import ru.ltmanagement.security.controller.request.LoginRequest;
import ru.ltmanagement.security.controller.response.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);
}
