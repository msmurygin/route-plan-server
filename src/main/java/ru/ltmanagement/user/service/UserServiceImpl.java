package ru.ltmanagement.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ltmanagement.user.dao.UserDao;
import ru.ltmanagement.user.dto.UserDto;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDAOImpl;

    @Override
    public UserDto getUserDetail(String loginId) {
        UserDto userDTO = UserDto.builder().build();
        userDTO.setLoginId("ltm");
        userDTO.setAdmin(userDAOImpl.isAdmin("ltm"));
        return userDTO;
    }
}
