package ru.ltmanagement.problems.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.problems.controller.request.ProblemListRequest;
import ru.ltmanagement.problems.dto.ProblemDto;

import java.util.List;

@Repository
public class ProblemDao {

    private static final String CONFIG_MAP_KEY_NAME =  "problemlist";
    private static final String SCHEMA_NAME = "wmwhse1";
    private static final String SQL_PROCEDURE_NAME = "rep_web_plan_route_reasons";


    private final JdbcTemplate jdbcTemplate;

    public ProblemDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<ProblemDto> CONTAINER_MAPPER = (rs, rowNum) ->
            ProblemDto.builder()
                    .rowNumber(rs.getInt("ROW_N"))
                    .sku(rs.getString("SKU"))
                    .descr(rs.getString("DESCR"))
                    .externalOrderKey(rs.getString("EXTERNALORDERKEY2"))
                    .openQty(rs.getBigDecimal("OPENQTY"))
                    .reasonMessage(rs.getString("REASON_MSG"))
                    .reasonCode(rs.getInt("REASON_CODE"))
                    .build();

    public List<ProblemDto> getProblems(ProblemListRequest request){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("route", request.getExternalLoadId());
        params.addValue("orderKey", request.getOrderKey());
        List<ProblemDto> replenishmentTaskList = (List<ProblemDto>) simpleJdbcCall.returningResultSet(CONFIG_MAP_KEY_NAME, CONTAINER_MAPPER)
                .execute(params).get(CONFIG_MAP_KEY_NAME);
        return replenishmentTaskList;
    }
}
