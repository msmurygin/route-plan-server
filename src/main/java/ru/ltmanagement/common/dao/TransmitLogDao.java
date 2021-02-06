package ru.ltmanagement.common.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransmitLogDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String SELECT_SQL = "SELECT count(1) FROM TRANSMITLOG (nolock) " +
            "WHERE KEY1 = :orderKey and key4=  '1' and KEY5 = 'orderpicked' ";


    public TransmitLogDao(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isOrderPickedSentToCustomerHost(String orderKey){
        MapSqlParameterSource params = new MapSqlParameterSource("orderKey", orderKey);
        Integer count = this.jdbcTemplate.queryForObject(SELECT_SQL, params, Integer.class);
        return count > 0;
    }
}
