package ru.ltmanagement.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserDto {
    private String loginId;
    private String fullyQualifiedName;
    private boolean isAdmin;
    private List<String> role;
}
