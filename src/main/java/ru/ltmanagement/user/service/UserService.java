package ru.ltmanagement.user.service;

import ru.ltmanagement.user.dto.UserDto;

public interface UserService {
    UserDto getUserDetail(String loginId);
}
