package ru.ltmanagement.security.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.user.dto.UserDto;

@Setter
@Getter
@AllArgsConstructor
public class AuthResponse {
    UserDto user;
    String token;
}
