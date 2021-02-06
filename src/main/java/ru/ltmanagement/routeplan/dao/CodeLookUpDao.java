package ru.ltmanagement.routeplan.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.routeplan.dto.CodeLookUpDto;

import java.util.List;

@Repository
public class CodeLookUpDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CodeLookUpDao(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<CodeLookUpDto> CODELKUP_MAPPER = (rs, rowNum) -> CodeLookUpDto.builder()
            .code(rs.getString("CODE"))
            .description(rs.getString("DESCRIPTION"))
            .build();

    public List<CodeLookUpDto> getCodeLookUp(String listName){
        MapSqlParameterSource params = new MapSqlParameterSource("listName", listName);
        return jdbcTemplate.query("select c.CODE, c.DESCRIPTION from CODELKUP c " +
                "where c.LISTNAME = :listName and c.ACTIVE = '1' order by c.CODE", params, CODELKUP_MAPPER);
    }

    public List<String> getRoutes() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbcTemplate.queryForList("select distinct o.ROUTE from PLAN_ROUTE_MAIN o (nolock) " +
                "where o.ROUTE not like 'СВ%' ", params, String.class);
    }


    public List<CodeLookUpDto> gerOrderStatus(){
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbcTemplate.query("select oss.CODE, trl.DESCRIPTION from ORDERSTATUSSETUP oss, TRANSLATIONLIST trl"+
                " WHERE oss.ENABLED = 1 and trl.TBLNAME = 'ORDERSTATUSSETUP' and trl.LOCALE='ru' and trl.COLUMNNAME = 'DESCRIPTION' "+
                " and oss.CODE = trl.JOINKEY1", params, CODELKUP_MAPPER);

    }
}
