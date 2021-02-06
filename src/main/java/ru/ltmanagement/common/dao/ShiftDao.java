package ru.ltmanagement.common.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.common.dto.ShiftTypeDto;

import java.time.LocalDateTime;

@Repository
public class ShiftDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ShiftDao(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SHIFT_CHECK_SQL = "select case when DATEPART(hour, :currentDate) >= (select top 1 SD_BEGIN from PLAN_ROUTE_SETTING)"+
            " and DATEPART(hour, :currentDate) < (select top 1 SD_END from PLAN_ROUTE_SETTING)  then 1 else 2 end";

    public ShiftTypeDto getShiftType(){
        LocalDateTime localDate = LocalDateTime.now();
        MapSqlParameterSource params = new MapSqlParameterSource("currentDate", localDate);
        String shiftCode = jdbcTemplate.queryForObject(SHIFT_CHECK_SQL, params, String.class);
        return ShiftTypeDto.of(shiftCode);
    }


}
