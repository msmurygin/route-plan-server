package ru.ltmanagement.user.dao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.user.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDaoImpl(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    String strQuery = "  select count(1)  from SCPRDD1.dbo.e_sso_user ud (nolock) " +
                      "  join SCPRDD1.dbo.e_sso_user_role ur (nolock) on ur.sso_user_id=ud.e_sso_user_id " +
                      "  join SCPRDD1.dbo.e_sso_role r (nolock) on r.e_sso_role_id=ur.sso_role_id " +
                      "  where ud.loginid = :loginId and r.sso_role_name =  'PlanRouteAdmin'";

    String SELECT_USER  = " SELECT  sso_user_name, fully_qualified_id, sso_hash_password from " +
                          " SCPRDD1.dbo.e_sso_user where loginid = :loginId ";


    String SELECT_ROLES = "select sso_role_name  from SCPRDD1.dbo.e_sso_user ud (nolock)\n" +
            "                       join SCPRDD1.dbo.e_sso_user_role ur (nolock) on ur.sso_user_id= ud.e_sso_user_id " +
            "                       join SCPRDD1.dbo.e_sso_role r (nolock) on r.e_sso_role_id=ur.sso_role_id " +
            "                       where ud.loginid = :login ";


    private static final RowMapper<UserDto> USER_MAPPER = (rs, rowNum) ->
            UserDto.builder()
                    .loginId(rs.getString("sso_user_name"))
                    .fullyQualifiedName(rs.getString("fully_qualified_id"))
                    .build();






    @Override
    @Cacheable("isAdmin")
    public boolean isAdmin(String loginId){
        MapSqlParameterSource params = new MapSqlParameterSource("loginId", loginId);
        return  jdbcTemplate.queryForObject(strQuery, params, Boolean.class);
    }

    @Override
    public Optional<UserDto> getUserByUserName(String loginId) {
        MapSqlParameterSource params = new MapSqlParameterSource("loginId", loginId);
        List<UserDto> userList = jdbcTemplate.query(SELECT_USER, params, USER_MAPPER);
        List<String> roles = getRoleByUserName(loginId).stream()
                .filter(role -> role.startsWith("PlanRoute"))
                .map(roleName -> roleName.endsWith("Admin") ? "ROLE_ADMIN": roleName)
                .map(roleName -> roleName.endsWith("User") ? "ROLE_USER": roleName)
                .collect(Collectors.toList());

        if (userList.size() == 1){
            UserDto userDto = userList.get(0);
            userDto.setRole(roles);
            userDto.setAdmin(
                    roles.stream()
                            .filter(role-> role.equalsIgnoreCase("ROLE_ADMIN"))
                            .count() > 0
            );
            return Optional.ofNullable(userDto);
        }
        return Optional.empty();
    }

    @Override
    @Cacheable
    public List<String> getRoleByUserName(String userName) {
        MapSqlParameterSource params = new MapSqlParameterSource("login", userName);
        List<String> roles = jdbcTemplate.query(SELECT_ROLES, params, (rs, rowNum) -> rs.getString("sso_role_name"));
        return roles;
    }


    @Override
    public UserDto getUser() {
        UserDto userDTO = UserDto.builder().build();
        userDTO.setLoginId("ltm");
        userDTO.setAdmin(isAdmin(userDTO.getLoginId()));
        return userDTO;
    }



}
