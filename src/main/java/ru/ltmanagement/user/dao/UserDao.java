package ru.ltmanagement.user.dao;

import ru.ltmanagement.user.dto.UserDto;

import java.util.List;

public interface UserDao {

    @Deprecated
    UserDto getUser();
    boolean isAdmin(String loginId);
    UserDto getUserByUserName(String userNam);
    List<String> getRoleByUserName(String userName);

}
