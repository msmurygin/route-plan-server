package ru.ltmanagement.ordermanagement.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.ordermanagement.dto.AllocationProcessingTypeResultDto;
import ru.ltmanagement.ordermanagement.dto.OrderCloseDto;

import java.util.List;

@Repository
public class OrderManagementDaoImpl implements OrderManagementDao {

    private static final String CLOSE_ORDER_KEY_NAME =  "closeOrder";
    private static final String ACTION_KEY_NAME =  "action";
    private static final String SCHEMA_NAME = "wmwhse1";
    private static final String SQL_PROCEDURE_NAME = "rep_web_plan_route_doActions";
    private static final String SQL_CLOSE_PROCEDURE_NAME = "rep_web_plan_route_for_close_order";

    private static final String SELECT_ORDER_WITH_PICK_IN_PROCESS = "select " +
                                                    " distinct orderkey from TASKDETAIL t (nolock) " +
                                                    " where     t.STATUS='3' and t.TASKTYPE='PK' and t.ORDERKEY in " +
                                                    " ( select ORDERKEY from PLAN_ROUTE_ORDER_LIST (nolock) " +
                                                    " where EXTERNALLOADID = :externalLoadId) ";

    private static final String SELECT_USERS_WITH_PICK_IN_PROCESS = " select u.USERNAME from TASKDETAIL t (nolock) " +
             "join VP_USERS u (nolock) on u.INFOR_LOGIN=t.USERKEY " +
             "where t.STATUS='3' and t.TASKTYPE='PK' and t.ORDERKEY in " +
             "(select ORDERKEY from PLAN_ROUTE_ORDER_LIST (nolock) where EXTERNALLOADID= :externalLoadId) " +
             "group by u.USERNAME ";

    private static final String SELECT_USERS_WITH_PICK_IN_PROCESS_BY_ORDER =  " SELECT u.USERNAME from TASKDETAIL t (nolock) " +
            "join VP_USERS u (nolock) on u.INFOR_LOGIN=t.USERKEY " +
            "where t.STATUS='3' and t.TASKTYPE = 'PK' and t.ORDERKEY= :orderKey " +
            "group by u.USERNAME ";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public OrderManagementDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }



    @Override
    public AllocationProcessingTypeResultDto getAllocationProcessingType(String externalLoadId, String orderKey, String userId, int action){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("externalloadid", externalLoadId);
        params.addValue("orderkey", orderKey);
        params.addValue("userid", userId);
        params.addValue("actions", action);
        params.addValue("is_route", StringUtils.isNotBlank(externalLoadId) ? 1 : 0 );
        String procedureResult  = ((List<String>)
                    simpleJdbcCall.returningResultSet(ACTION_KEY_NAME, (rs, rowNum) ->  rs.getString("StandartAction"))
                            .execute(params)
                            .get(ACTION_KEY_NAME))
                            .get(0);
        return AllocationProcessingTypeResultDto.valueOfFlag(procedureResult);
    }

    @Override
    public List<String> getOrderKeyInProcess(String externalLoadId){
        MapSqlParameterSource params = new MapSqlParameterSource("externalLoadId", externalLoadId);
        return namedParameterJdbcTemplate.query(SELECT_ORDER_WITH_PICK_IN_PROCESS, params,  (rs, rowNum) ->  rs.getString("orderkey"));
    }

    @Override
    public List<String> getUserNamesPickInProcess(String externalLoadId) {
        MapSqlParameterSource params = new MapSqlParameterSource("externalLoadId", externalLoadId);
        return namedParameterJdbcTemplate.query(SELECT_USERS_WITH_PICK_IN_PROCESS, params,  (rs, rowNum) ->  rs.getString("USERNAME"));
    }

    @Override
    public List<String> getUserListPickingOrder(String orderKey) {
        MapSqlParameterSource params = new MapSqlParameterSource("orderKey", orderKey);
        return namedParameterJdbcTemplate.query(SELECT_USERS_WITH_PICK_IN_PROCESS_BY_ORDER, params,  (rs, rowNum) ->  rs.getString("USERNAME"));
    }


    private static final RowMapper<OrderCloseDto> CLOSE_ORDER_MAPPER = (rs, rowNum) ->
            OrderCloseDto.builder()
                    .orderKey(rs.getString("orderkey"))
                    .allowBackOrder(rs.getInt("allowBackOrder"))
                    .status(rs.getString("status"))
                    .backOrderType(rs.getInt("backOrderType"))
                    .type(rs.getString("type"))
                    .reasonCodeFlag(rs.getInt("reasonCodeFlag"))
                    .shipTogetherFlag(rs.getInt("shipTogetherFlag"))
                    .qtyPickInProcess(rs.getInt("qtyPickInprocess"))
                    .pickStatusFlag(rs.getInt("pickStatusFlag"))
                    .mayCloseOrder(rs.getInt("mayCloseOrder"))
                    .build();
    @Override
    public List<OrderCloseDto> getOrdersForClose(String externalLoadId, String orderKey) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_CLOSE_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("externalid", externalLoadId);
        params.addValue("orderkey", orderKey);
        return  (List<OrderCloseDto>) simpleJdbcCall.returningResultSet(CLOSE_ORDER_KEY_NAME, CLOSE_ORDER_MAPPER)
                .execute(params).get(CLOSE_ORDER_KEY_NAME);
    }


}
