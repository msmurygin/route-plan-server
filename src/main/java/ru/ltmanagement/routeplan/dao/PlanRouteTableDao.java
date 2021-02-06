package ru.ltmanagement.routeplan.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.routeplan.dto.DetailTableDto;
import ru.ltmanagement.routeplan.dto.HeaderTableDto;
import ru.ltmanagement.routeplan.dto.OrderListPutRequestDto;
import ru.ltmanagement.routeplan.dto.PlanRouteConfigurationDto;
import ru.ltmanagement.routeplan.dto.RoutePlanRequestDto;
import ru.ltmanagement.user.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PlanRouteTableDao {
    private final static String SELF_DELIVERY = "Самовывоз";

    private static final String DELIVERY_DATE_BETWEEN = " DELIVERYDATE between :startDate and :endDate ";
    private static final String DELIVERY_DATE = " DELIVERYDATE >= :startDate ";
    private static final String NOT_FINISHED_TASK_SQL =" EXTERNALLOADID in (select distinct EXTERNALLOADID from PLAN_ROUTE_ORDER_LIST " +
            "where ORDERKEY in (select distinct ORDERKEY from ORDERDETAIL (nolock) where OPENQTY > 0) ) ";


    private static final String HEADER_SELECT = "select  sum(kol_strok) as TASK_PLAN, sum(fact_task) as TASK_FAKT, " +
            "sum(kol_strok)-sum(fact_task) as TASK_OST, sum(plan_wgt) as WGT_PLAN, sum(STDGROSSWGT) as WGT_FACT, " +
            "sum(ost_wgt) as WGT_OST, sum(plan_cube) as CUBE_PLAN, sum(STDCUBE) as CUBE_FACT, " +
            "sum(ost_cube) as CUBE_OST from PLAN_ROUTE_MAIN WHERE %s ";

    private static final String DETAIL_SELECT = "select o.ROW_N, o.EXTERNALLOADID,  o.DELIVERYDATE as DELIVERYDATE, " +
            "o.LOADUSR2,  o.ROUTE,  o.replenisment_task,  o.REASON_CODE, o.SHOW_REASON,  o.STDCUBE, o.STDGROSSWGT, " +
            "o.sobrano,  o.kontrol,  o.ypakovano, o.zagrujeno,  o.STATUS,  o.kol_strok,  o.PACKINGLOCATION, o.DOOR, " +
            "o.SUSR2,  o.DRIVERNAME,  o.LOADUSR1,  o.ADDDATE as ADDDATE,  o.READY_ROUTE as READY_ROUTE, " +
            "o.ACTUALARRIVALDATE as ACTUALARRIVALDATE,  o.BEGIN_LOAD as BEGIN_LOAD,  " +
            "o.END_LOAD as END_LOAD,  o.BETWEEN_BEGIN_END_LOAD, o.SHIPED as SHIPED,  " +
            "o.GONE_TS as GONE_TS,  o.SELECTEDCARTONID_QTY,  o.SMENA,  o.CLOSED_ROUTE,  " +
            "o.NEED_PICK_QTY,  o.NEED_CONTROL_QTY  from PLAN_ROUTE_MAIN o where %s order by o.DELIVERYDATE";

    private static final String UPDATE_TABLE_SQL = "UPDATE PLAN_ROUTE_MAIN set ACTUALARRIVALDATE =:actualArrivalDate," +
            "GONE_TS =:truckLeavingDate, SUSR2 =:susr2, DOOR =:door, PACKINGLOCATION =:loc WHERE ROW_N =:id";
    private static final String UPDATE_ORDER_LIST_SQL = "UPDATE PLAN_ROUTE_ORDER_LIST SET DOOR=:door, PACKINGLOCATION=:loc," +
            "ACTUALARRIVALDATE=:arrival, GONE_TS=:leaving, editdate=:editdate, STOP=:stop WHERE ORDERKEY = :orderkey";

    private static final String AND = " AND ";
    private static final String SQL_IN_VALUES_SEPARATOR = ",";


    @Value("${date.format}")
    private String datePattern;


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PlanRouteTableDao(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<DetailTableDto> DETAIL_MAPPER = (rs, rowNum) ->
            DetailTableDto.builder()
                    .rowId(rs.getString("ROW_N"))
                    .stdCube(rs.getBigDecimal("STDCUBE"))
                    .actualArrivalDate(dateToString(rs.getDate("ACTUALARRIVALDATE")))
                    .addDate(dateToString(rs.getDate("ADDDATE")))
                    .loadStart(dateToString(rs.getDate("BEGIN_LOAD")))
                    .loadDuration(rs.getString("BETWEEN_BEGIN_END_LOAD"))
                    .routeClosed(rs.getInt("CLOSED_ROUTE"))
                    .deliveryDate(dateToString(rs.getDate("DELIVERYDATE")))
                    .door(rs.getString("DOOR"))
                    .driverName(rs.getString("DRIVERNAME"))
                    .loadEnd(dateToString(rs.getDate("END_LOAD")))
                    .externalloadid(rs.getString("EXTERNALLOADID"))
                    .truckLeaving(dateToString(rs.getDate("GONE_TS")))
                    .itemsInRoute(rs.getInt("kol_strok"))
                    .controlled(rs.getInt("kontrol"))
                    .loadUsr1(rs.getString("LOADUSR1"))
                    .loadUsr2(rs.getString("LOADUSR2"))
                    .leftToControl(rs.getInt("NEED_CONTROL_QTY"))
                    .leftToPick(rs.getInt("NEED_PICK_QTY"))
                    .packingLocation(rs.getString("PACKINGLOCATION"))
                    .routeReady(dateToString(rs.getDate("READY_ROUTE")))
                    .reasonCode(rs.getInt("REASON_CODE"))
                    .replenishmentTask(rs.getString("replenisment_task"))
                    .route(rs.getString("ROUTE"))
                    .shippedItems(rs.getInt("SELECTEDCARTONID_QTY"))
                    .shipped(dateToString(rs.getDate("SHIPED")))
                    .showReason(rs.getInt("SHOW_REASON"))
                    .shift(rs.getString("SMENA"))
                    .picked(rs.getInt("sobrano"))
                    .status(rs.getString("STATUS"))
                    .stdGrossWgt(rs.getBigDecimal("STDGROSSWGT"))
                    .susr2(rs.getString("SUSR2"))
                    .packed(rs.getInt("ypakovano"))
                    .loaded(rs.getInt("zagrujeno"))
                    .build();

    private static final RowMapper<HeaderTableDto> HEADER_MAPPER = (rs, rowNum) ->
            HeaderTableDto.builder()
                    .planedTasks(rs.getInt("TASK_PLAN"))
                    .realTasks(rs.getInt("TASK_FAKT"))
                    .remindedTasks(rs.getInt("TASK_OST"))
                    .planedWeight(rs.getBigDecimal("WGT_PLAN"))
                    .realWeight(rs.getBigDecimal("WGT_FACT"))
                    .remindedWeight(rs.getBigDecimal("WGT_OST"))
                    .planedCube(rs.getBigDecimal("CUBE_PLAN"))
                    .realCube(rs.getBigDecimal("CUBE_FACT"))
                    .remindedCube(rs.getBigDecimal("CUBE_OST"))
                    .build();


    public List<DetailTableDto> getDetailTable(RoutePlanRequestDto req, PlanRouteConfigurationDto config, UserDto user){
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =  buildSql(DETAIL_SELECT, req, config, user, params);
        return jdbcTemplate.query(sql, params, DETAIL_MAPPER);
    }

    public HeaderTableDto getHeaderTable(RoutePlanRequestDto req, PlanRouteConfigurationDto config, UserDto user){
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =  buildSql(HEADER_SELECT, req, config, user, params);
        return jdbcTemplate.queryForObject(sql, params, HEADER_MAPPER);
    }


    public String buildSql(String initialSql, RoutePlanRequestDto req, PlanRouteConfigurationDto config, UserDto user,
                           MapSqlParameterSource params){

        LocalDateTime startDate = LocalDate.now()
                .atTime(0,0);
        LocalDateTime endDate;

        params.addValue("startDate", startDate);
        String WHERE =  DELIVERY_DATE;

        if (StringUtils.isNoneEmpty(req.getStartPeriod())){
            startDate = fromStringToLocalDateTime(req.getStartPeriod());
            params.addValue("startDate", startDate);
        }

        if (StringUtils.isNoneEmpty(req.getEndPeriod())){
            WHERE = DELIVERY_DATE_BETWEEN;
            endDate = fromStringToLocalDateTime(req.getEndPeriod());
            params.addValue("endDate", endDate);
        }

        if (req.isByShifts()){
            WHERE = " DELIVERYDATE between :startDate and dateadd(hour, 36, :startDate)";
            params.addValue("startDate", startDate);

            if (user.isAdmin()){
                if (Not(config.isHideNotFinishedTask())){
                    WHERE = NOT_FINISHED_TASK_SQL;

                    if (config.isShowLastShiftNotFinishedTask()){
                        WHERE +=" and DELIVERYDATE between dateadd(hour,-12, :startDate) and :startDate";
                    }
                    if (config.isShowNotFinishedTaskInPeriod()){
                        WHERE += StringUtils.isNoneEmpty(req.getEndPeriod()) ?
                                AND + DELIVERY_DATE_BETWEEN :
                                AND + DELIVERY_DATE;
                    }
                }
                if (Not(config.isHidePlanedTasks())){
                    String someWhere = NOT_FINISHED_TASK_SQL;
                    WHERE = someWhere + AND + DELIVERY_DATE;
                    if (config.isShowNextShiftPlanedTasks()){
                        WHERE= someWhere+" and DELIVERYDATE between dateadd(hour,12,:startDate) and dateadd(hour, 24, :startDate)";
                    }else if (config.isShowPlanedTaskInPeriod()){
                        WHERE += StringUtils.isNoneEmpty(req.getEndPeriod()) ?
                                AND + DELIVERY_DATE_BETWEEN :
                                AND + DELIVERY_DATE;

                    }
                }
            }
        }

        if (!req.getOrderType().isEmpty()){
            WHERE += String.format(" AND EXTERNALLOADID in (select EXTERNALLOADID from ORDERS (nolock) where TYPE in (%s))",
                    inSql(req.getOrderType()));
        }

        if (!req.getDestination().isEmpty()){
            WHERE += getDeliveryRouteSql(req);
        }

        if(!req.getOrderStatus().isEmpty()) {
            WHERE += String.format(" AND EXTERNALLOADID in (select EXTERNALLOADID from ORDERS (nolock) where STATUS in (%s))",
                    inSql(req.getOrderStatus()));
        }

        return String.format(initialSql, WHERE);
    }



    private String getDeliveryRouteSql(RoutePlanRequestDto request) {
        String  destination = request.getDestination().stream()
                .map(this::wrapInQuotes)
                .collect(Collectors.joining(SQL_IN_VALUES_SEPARATOR));
        boolean hasSelfDelivery =  request.getDestination().contains(SELF_DELIVERY);
        boolean onlySelfDelivery = hasSelfDelivery && request.getDestination().size() == 1;
        return onlySelfDelivery ? " AND EXTERNALLOADID = LOADUSR2" :
                hasSelfDelivery ? String.format(" AND (ROUTE in (%s) or EXTERNALLOADID = LOADUSR2) ", destination) :
                        String.format(" AND ROUTE in (%s)", destination);
    }

    public LocalDateTime fromStringToLocalDateTime(String date){
       return Optional.ofNullable(date).map(value ->{
            DateTimeFormatter dateFormat  = DateTimeFormatter.ofPattern(datePattern);
            LocalDate dateResult = LocalDate.parse(value, dateFormat);
            LocalDateTime localDateTime = dateResult.atTime(0,0);
            return localDateTime; //localDateTime;
        }).orElse(null);
    }

    public String dateToString(@Nullable java.sql.Date date){
        return  Optional.ofNullable(date).map(value -> {
            LocalDate localDate = date.toLocalDate();
            DateTimeFormatter dateFormat  = DateTimeFormatter.ofPattern(datePattern);
            return dateFormat.format(localDate);
        }).orElse(null);
    }

    public String inSql(List<String> values){
        String val =  values.stream()
                .map(this::wrapInQuotes)
                .collect(Collectors.joining(SQL_IN_VALUES_SEPARATOR));
        return val;
    }

    private boolean Not(boolean b) {
        return  !b;
    }

    private String wrapInQuotes(String value) {
        return value.length() > 0 ? "'" + value + "'" : value;
    }

    public void updateTable(List<DetailTableDto> details) {
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[details.size()];
        for (int i = 0; i < details.size(); i++) {
            DetailTableDto detail = details.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("truckLeavingDate", detail.getTruckLeaving())
                    .addValue("actualArrivalDate", detail.getActualArrivalDate())
                    .addValue("door", detail.getDoor())
                    .addValue("loc", detail.getPackingLocation())
                    .addValue("susr2", detail.getSusr2())
                    .addValue("id", detail.getRowId());
            paramsArray[i] = params;
        }
        jdbcTemplate.batchUpdate(UPDATE_TABLE_SQL, paramsArray);
    }

    public void updateOrderList(List<OrderListPutRequestDto> orderList) {
        MapSqlParameterSource[] paramsArray = new MapSqlParameterSource[orderList.size()];
        for (int i = 0; i < orderList.size(); i++) {
            OrderListPutRequestDto detail = orderList.get(i);
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("door", detail.getDoor())
                    .addValue("stop", detail.getStop())
                    .addValue("loc", detail.getPackingLocation())
                    .addValue("arrival", detail.getVehicleArrival())
                    .addValue("leaving", detail.getVehicleLeaving())
                    .addValue("editdate", LocalDateTime.now())
                    .addValue("orderkey", detail.getOrderKey());
            paramsArray[i] = params;
        }
        jdbcTemplate.batchUpdate(UPDATE_ORDER_LIST_SQL, paramsArray);
    }
}
