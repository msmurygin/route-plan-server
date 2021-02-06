package ru.ltmanagement.settings.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.settings.NSqlConfigDto;

@Repository
public class NSqlConfigDao {

    private static final RowMapper<NSqlConfigDto> ROW_MAPPER = (rs, rowNum) -> NSqlConfigDto.builder()
            .nSqlValue(rs.getString("NSQLVALUE"))
            .nSqlDescript(rs.getString("NSQLDESCRIP"))
            .build();
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public NSqlConfigDao(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    public NSqlConfigDto getNSqlConfig(String configKey){
        MapSqlParameterSource params = new MapSqlParameterSource("configKey", configKey);
        return jdbcTemplate
                .query("SELECT NSQLVALUE, NSQLDESCRIP FROM NSQLCONFIG WHERE CONFIGKEY=:configKey", params, ROW_MAPPER)
                .get(0);
    }

}
