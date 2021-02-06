package ru.ltmanagement.claims.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.claims.dto.ClaimDto;
import ru.ltmanagement.claims.dto.ClaimsDetailDto;
import ru.ltmanagement.claims.dto.ClaimsRequestDto;
import ru.ltmanagement.claims.dto.ClaimsSkuDetailDto;
import ru.ltmanagement.claims.dto.SkuFilterDto;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClaimsDao {

    @Value("${date.format}")
    private String datePattern;

    private ZoneId defaultZoneId = ZoneId.systemDefault();
    private static final String SELECT_SKU_CLAIMS = "select distinct c.SKU from COMPLAINT c";

    private static final String ACTION_KEY_NAME =  "action";
    private static final String SCHEMA_NAME = "wmwhse1";
    private static final String SQL_PROCEDURE_NAME = "report_pretension_t1_MainTable";
    private static final String SQL_DETAIL_PROCEDURE_NAME = "report_pretension_t2_PretDetail";
    private static final String SQL_DETAIL_BY_SKU_PROCEDURE_NAME = "report_pretension_t3_RezultInv";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<ClaimDto> CLAIMS_MAPPER = (rs, rowNum) ->
            ClaimDto.builder()
                    .id(rs.getString("NUMCOMPLAINT"))
                    .externalLoadId(rs.getString("EXTERNALLOADID"))
                    .client(rs.getString("COMPANY_LONG"))
                    .addDate(rs.getString("ADDDATE"))
                    .dateOfArrival(rs.getString("ADDDATETIME"))
                    .linesCount(rs.getString("LINE_COUNT"))
                    .linesCountChecked(rs.getString("TASK_COMPLITE_COUNT"))
                    .cost(rs.getString("SUMPRICE"))
                    .taskState(rs.getInt("TASK_STATE"))
                    .build();

    private static final RowMapper<ClaimsDetailDto> CLAIMS_DETAIL_MAPPER = (rs, rowNum) ->
            ClaimsDetailDto.builder()
                    .serialKey(rs.getInt("SERIALKEY"))
                    .externOrderKey(rs.getString("EXTERNORDERKEY"))
                    .sku(rs.getString("SKU"))
                    .descr(rs.getString("DESCR"))
                    .qtyPicked(rs.getBigDecimal("QTYPICKED"))
                    .qtyFact(rs.getBigDecimal("QTYFACT"))
                    .sumPrice(rs.getBigDecimal("SUMPRICE"))
                    .viewClaims(rs.getString("VIEWCOMPLAINT"))
                    .commentIn(rs.getString("COMMENTIN"))
                    .whoseFault(rs.getString("WHOSEFAULT"))
                    .reason(rs.getString("REASON"))
                    .response(rs.getString("RESPONSE"))
                    .guiltyPick(rs.getString("GUILTYPICK"))
                    .guiltyStoreKeeper(rs.getString("GUILTYSTOREKEEPER"))
                    .guiltyController(rs.getString("GUILTYCONTROLLER"))
                    .auditor(rs.getString("AUDITOR"))
                    .mayChange(rs.getString("MAY_CHANGE"))
                    .build();


    private final RowMapper<ClaimsSkuDetailDto> CLAIMS_DETAIL_BY_SKU_MAPPER = (rs, rowNum) ->
            ClaimsSkuDetailDto.builder()
                    .id(rs.getString("ROW_N"))
                    .sku(rs.getString("SKU"))
                    .descr(rs.getString("DESCR"))
                    .loc(rs.getString("LOC"))
                    .qty(rs.getBigDecimal("QTY"))
                    .tdQty(rs.getBigDecimal("TD_QTY"))
                    .result(rs.getString("RESULT"))
                    .tdStatus(rs.getString("TD_STATUS"))
                    .tdDate(rs.getString("TD_DATE"))
                    .reason(rs.getString("REASON"))
                    .auditor(rs.getString("AUDITOR"))
                    .build();


    public ClaimsDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }




    @CacheEvict("skuClaims")
    public List<String> getSkus(){
        MapSqlParameterSource params = new MapSqlParameterSource();
        return this.namedParameterJdbcTemplate.query(SELECT_SKU_CLAIMS,params, (rs, index) -> rs.getString("SKU"));
    }

    public List<ClaimDto> getClaims(ClaimsRequestDto req){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("start_date", fromStringToLocalDateTime(req.getStartDate()), Types.TIMESTAMP)
            .addValue("end_date", fromStringToLocalDateTime(req.getEndDate()), Types.TIMESTAMP)
            .addValue("all_date", req.getAllDates())
            .addValue("client", req.getClient())
            .addValue("show_check", req.getShowCheck())
            .addValue("show_send", req.getShowSend())
            .addValue("show_all", req.getShowAll())
            .addValue("skus", joinStrings(req.getSkus()))
            .addValue("guilty", req.getGuilty());
        List<ClaimDto>  claims = (List<ClaimDto>)simpleJdbcCall.returningResultSet(ACTION_KEY_NAME, CLAIMS_MAPPER)
                        .execute(params)
                        .get(ACTION_KEY_NAME);
        return claims;
    }

    public List<ClaimDto> getClaimsDetail(String claimsNumber){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_DETAIL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        SqlParameterSource params = new MapSqlParameterSource("pretension_number",claimsNumber);
        List<ClaimDto>  claims = (List<ClaimDto>)simpleJdbcCall.returningResultSet(ACTION_KEY_NAME, CLAIMS_DETAIL_MAPPER)
                .execute(params)
                .get(ACTION_KEY_NAME);
        return claims;
    }


    public List<ClaimsSkuDetailDto> getClaimsDetailBySku(String claimsNumber, String sku){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(SQL_DETAIL_BY_SKU_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        SqlParameterSource params = new MapSqlParameterSource("sku",sku)
                .addValue("pretension_number", claimsNumber);
        List<ClaimsSkuDetailDto>  claims = (List<ClaimsSkuDetailDto>)simpleJdbcCall.returningResultSet(ACTION_KEY_NAME, CLAIMS_DETAIL_BY_SKU_MAPPER)
                .execute(params)
                .get(ACTION_KEY_NAME);
        return claims;
    }

    private LocalDateTime fromStringToLocalDateTime(String date){
        return Optional.ofNullable(date).map(value ->{
            DateTimeFormatter dateFormat  = DateTimeFormatter.ofPattern(datePattern);
            LocalDate dateResult = LocalDate.parse(value, dateFormat);
            LocalDateTime localDateTime = dateResult.atTime(0,0);
            return localDateTime;
        }).orElse(null);
    }


    public String joinStrings(List<SkuFilterDto> skus){
        return  Optional.ofNullable(skus.stream()
                .map(item -> item.getName())
                .collect(Collectors.joining(", "))
        ).orElse("");
    }
}
