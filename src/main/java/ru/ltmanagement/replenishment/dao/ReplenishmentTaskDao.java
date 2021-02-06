package ru.ltmanagement.replenishment.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.replenishment.dto.ReplenishmentPriorityDto;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskDto;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskRequestDto;
import ru.ltmanagement.user.dao.UserDao;
import ru.ltmanagement.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReplenishmentTaskDao {
    private static final String UPDATE_PRIORITY_SQL = "UPDATE SKUXLOC SET REPLENISHMENTPRIORITY =:priority, " +
            "EDITWHO = :editwho, EDITDATE = :editdate WHERE SERIALKEY = :serialkey AND REPLENISHMENTPRIORITY <= 4  ";
    @Autowired
    private UserDao userDao;
    
    private static final String INIT_STRING = "";
    private static final String CONFIG_MAP_KEY_NAME =  "replenishment";
    private static final String SCHEMA_NAME = "wmwhse1";
    private static final String SQL_PROCEDURE_NAME = "rep_web_plan_route_replenishment";
    private static final String SELECT_REPLENISHMENT_PRIORITY =
            " SELECT locationtype ,SERIALKEY ,sku ,descr  ,loc, qty, REPLENISHMENTSEVERITY ,REPLENISHMENTPRIORITY, " +
                    " PUTAWAYZONE, logicallocation FROM WMS_REPLENISHMENT_PRIORITY WHERE 1 = 1 ";
    private static final String AND_SKU_CONDITION = " AND sku = :sku";
    private static final String AND_LOC_CONDITION = " AND loc = :loc";
    private static final String AND_ZONE_CONDITION = " AND putawayzone = :zone";

    private static final RowMapper<ReplenishmentPriorityDto> SELECT_REPL_MAPPER = (rs, rowNum) ->
            ReplenishmentPriorityDto.builder()
                    .serialKey(rs.getInt("SERIALKEY"))
                    .sku(rs.getString("sku"))
                    .loc(rs.getString("loc"))
                    .descr(rs.getString("descr"))
                    .locationType(rs.getString("locationtype"))
                    .qty(rs.getBigDecimal("qty"))
                    .replenishmentQty(rs.getBigDecimal("REPLENISHMENTSEVERITY"))
                    .priority(rs.getString("REPLENISHMENTPRIORITY"))
                    .putawayZone(rs.getString("PUTAWAYZONE"))
                    .bypassRoute(rs.getString("logicallocation"))
                    .build();


    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public ReplenishmentTaskDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    private static final RowMapper<ReplenishmentTaskDto> CONTAINER_MAPPER = (rs, rowNum) ->
            ReplenishmentTaskDto.builder()
                    .ROW_N(rs.getString("ROW_N"))
                    .SKU(rs.getString("SKU"))
                    .DESCR(rs.getString("DESCR"))
                    .LOC(rs.getString("LOC"))
                    .PUTAWAYZONE(rs.getString("PUTAWAYZONE"))
                    .REPLENISHMENTPRIORITY(rs.getString("REPLENISHMENTPRIORITY"))
                    .build();

    public List<ReplenishmentTaskDto> getReplenishmentTask(String loginId, ReplenishmentTaskRequestDto requestDto){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("order", requestDto.getOrderKey());
        params.addValue("routenum", requestDto.getExternLoadId());
        params.addValue("change_priority", requestDto.isChangePriority());
        params.addValue("priotity", requestDto.getPriorityValue());
        params.addValue("user_name", loginId);
        List<ReplenishmentTaskDto> replenishmentTaskList = (List<ReplenishmentTaskDto>) simpleJdbcCall.returningResultSet(CONFIG_MAP_KEY_NAME, CONTAINER_MAPPER)
                .execute(params).get(CONFIG_MAP_KEY_NAME);
        return replenishmentTaskList;
    }


    public List<ReplenishmentPriorityDto> getReplenishmentPriority(String sku, String loc, String zone){
        MapSqlParameterSource params = new MapSqlParameterSource();
        String query = SELECT_REPLENISHMENT_PRIORITY;
        if (!StringUtils.isEmpty(sku)){
            query += AND_SKU_CONDITION;
            params.addValue("sku", sku);
        }
        if (!StringUtils.isEmpty(loc)){
            query += AND_LOC_CONDITION;
            params.addValue("loc", loc);
        }
        if (!StringUtils.isEmpty(zone)){
            query += AND_ZONE_CONDITION;
            params.addValue("zone", zone);
        }
        return namedParameterJdbcTemplate.query(query, params, SELECT_REPL_MAPPER);
    }

    public void updateReplenishmentPriority(@NotNull List<ReplenishmentPriorityDto> replenishmentUpdate) {
        UserDto user = userDao.getUser();
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[replenishmentUpdate.size()];
        for (int i = 0; i < replenishmentUpdate.size(); i++) {
            ReplenishmentPriorityDto replenishmentPriorityDto = replenishmentUpdate.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("priority", replenishmentPriorityDto.getPriority())
                    .addValue("serialkey", replenishmentPriorityDto.getSerialKey())
                    .addValue("editwho", user.getLoginId())
                    .addValue("editdate", LocalDateTime.now());
            paramsArray[i] = params;
        }
        namedParameterJdbcTemplate.batchUpdate(UPDATE_PRIORITY_SQL, paramsArray);
    }
}
