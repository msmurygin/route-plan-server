package ru.ltmanagement.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.routeplan.dto.ActiveUserDto;

import java.util.List;


@Repository
public class ActiveUsersDao {
    private static final String CONFIG_MAP_KEY_NAME =  "activeusers";
    private static final String SCHEMA_NAME = "wmwhse1";
    private static final String SQL_PROCEDURE_NAME = "rep_web_plan_route_active_users";
    private final JdbcTemplate jdbcTemplate;

    public ActiveUsersDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<ActiveUserDto> ACTIVE_USERS_MAPPER = (rs, rowNum) ->
            ActiveUserDto.builder()
                    .mulinet(rs.getInt("MULINET"))
                    .forkLift(rs.getInt("RICH"))
                    .radioDevices(rs.getInt("RF"))
                    .build();

    public ActiveUserDto getActiveUsers() {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        ActiveUserDto activeUserDto = ((List<ActiveUserDto>) simpleJdbcCall.returningResultSet(CONFIG_MAP_KEY_NAME, ACTIVE_USERS_MAPPER)
                .execute(params).get(CONFIG_MAP_KEY_NAME)).get(0);
        return activeUserDto;
    }
}
