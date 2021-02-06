package ru.ltmanagement.routeplan.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.routeplan.dto.SkuStockDto;

import java.util.List;

@Repository
public class SkuStockDao {
    private static final String CONFIG_MAP_KEY_NAME =  "skustock";
    private static final String SCHEMA_NAME = "wmwhse1";
    private static final String SQL_PROCEDURE_NAME = "rep_web_plan_route_sku_on_balance";
    private final JdbcTemplate jdbcTemplate;

    public SkuStockDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    private static final RowMapper<SkuStockDto> SKU_MAPPER = (rs, rowNum) ->
            SkuStockDto.builder()
                    .DESCR_LOC(rs.getString("DESCR_LOC"))
                    .DESCR_ZONE(rs.getString("DESCR_ZONE"))
                    .LOC(rs.getString("LOC"))
                    .PUTAWAYZONE(rs.getString("PUTAWAYZONE"))
                    .QTY(rs.getBigDecimal("QTY"))
                    .QTY_BALANCE(rs.getBigDecimal("QTY_BALANCE"))
                    .QTYALLOCATED(rs.getBigDecimal("QTYALLOCATED"))
                    .QTYPICKED(rs.getBigDecimal("QTYPICKED"))
                    .STATUS(rs.getString("STATUS"))
                    .build();

    public List<SkuStockDto> getSkuStock(String sku){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource("sku", sku);
        List<SkuStockDto> skuStockList = (List<SkuStockDto>)
                simpleJdbcCall.returningResultSet(CONFIG_MAP_KEY_NAME, SKU_MAPPER)
                        .execute(params).get(CONFIG_MAP_KEY_NAME);
        return skuStockList;
    }

}
