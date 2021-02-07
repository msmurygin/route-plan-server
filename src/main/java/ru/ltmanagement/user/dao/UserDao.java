package ru.ltmanagement.user.dao;

import ru.ltmanagement.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    @Deprecated
    UserDto getUser();
    boolean isAdmin(String loginId);
    Optional<UserDto> getUserByUserName(String userNam);
    List<String> getRoleByUserName(String userName);

}
