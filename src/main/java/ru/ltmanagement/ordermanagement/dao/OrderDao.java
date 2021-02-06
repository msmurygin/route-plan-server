package ru.ltmanagement.ordermanagement.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import ru.ltmanagement.ordermanagement.dto.OrderDetailDto;
import ru.ltmanagement.ordermanagement.dto.OrderDto;
import ru.ltmanagement.ordermanagement.dto.OrderLineDto;
import ru.ltmanagement.settings.dao.NSqlConfigDao;
import ru.ltmanagement.user.dao.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderDao {
    private static final String ORDER_DETAIL_MAP_KEY_NAME =  "configkey";
    private static final String WAVE_MAP_KEY_NAME =  "wavekey";
    private static final String SCHEMA_NAME = "wmwhse1";
    private static final String ORDER_DETAIL_SQL_PROCEDURE_NAME = "rep_web_plan_route_order_detail";
    private static final String WAVES_SQL_PROCEDURE_NAME = "rep_web_plan_route_doRelease";
    private static final String CONFIGKEY = "LT_LANEVOLUME" ;

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public OrderDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Autowired
    private NSqlConfigDao nSqlConfigDao;

    @Autowired
    private UserDao userDao;

    private static double nSqlValue = 0;



    private static final String  SELECT_ORDER_DETAIL_LIST = "SELECT STOP," +
            "ROUTE, orderkey, EXTERNALORDERKEY2, TOTALORDERLINES, sobrano, kontrol, ypakovano, zagrujeno, " +
            "PACKINGLOCATION, door  , STDCUBE, STDGROSSWGT ,SELECTEDCARTONID_QTY, status, susr2,ADDDATE, READY_ROUTE, " +
            "ACTUALARRIVALDATE, BEGIN_LOAD, END_LOAD, BETWEEN_BEGIN_END_LOAD, SHIPED, GONE_TS,CLOSED_ORDER," +
            "NEED_CONTROL_QTY, NEED_PICK_QTY ,REASON_CODE, SHOW_REASON " +
            "from PLAN_ROUTE_ORDER_LIST  where EXTERNALLOADID= :externalLoadId ";

    private static final String SELECT_ORDERS = "SELECT o.ORDERKEY from ORDERS o (nolock) where o.EXTERNALLOADID = :externalLoadId ";
    private static final String SELECT_ADMIN_DELIVERY = "SELECT count(1) from ORDERDETAIL (nolock) where ORDERKEY = :orderKey ";
    private static final String UPDATE_ORDER_STATUS = "UPDATE ORDERS set STATUS=:status, EDITWHO = :userKey, EDITDATE = getutcdate() where ORDERKEY=:orderKey";
    private static final String SELECT_LOADID = "SELECT TOP 1 l.LOADID from ORDERS l (nolock) where l.EXTERNALLOADID = :extLoadId ";

    private static final RowMapper<OrderLineDto> ORDERS_DETAIL_MAPPER = (rs, rowNum) -> OrderLineDto.builder()
            .stop(rs.getInt("STOP"))
            .orderKey(rs.getString("orderkey"))
            .externalOrderKey2(rs.getString("EXTERNALORDERKEY2"))
            .orderLines(rs.getInt("TOTALORDERLINES"))
            .orderClosed(rs.getInt("CLOSED_ORDER"))
            .door(rs.getString("DOOR"))
            .vehicleLeftDate(rs.getString("GONE_TS"))
            .controlledQty(rs.getDouble("kontrol"))
            .leftToControl(rs.getDouble("NEED_CONTROL_QTY"))
            .leftToPick(rs.getDouble("NEED_PICK_QTY"))
            .packingLocation(rs.getString("PACKINGLOCATION"))
            .reasonCode(rs.getInt("REASON_CODE"))
            .showReason(rs.getInt("SHOW_REASON"))
            .actualArrivalDate(rs.getString("ACTUALARRIVALDATE"))
            .route(rs.getString("ROUTE"))
            .selectedCartonIdQty(rs.getInt("SELECTEDCARTONID_QTY"))
            .addDate(rs.getString("ADDDATE"))
            .shipDate(rs.getString("SHIPED"))
            .loadStart(rs.getString("BEGIN_LOAD"))
            .loadEnd(rs.getString("END_LOAD"))
            .routeReady(rs.getString("READY_ROUTE"))
            .loadDuration(rs.getString("BETWEEN_BEGIN_END_LOAD"))
            .status(rs.getString("STATUS"))
            .stdCube(rs.getDouble("STDCUBE"))
            .stdGrossWgt(rs.getDouble("STDGROSSWGT"))
            .susr2(rs.getString("SUSR2"))
            .pickedQty(rs.getDouble("sobrano"))
            .packedQty(rs.getDouble("ypakovano"))
            .controlledQty(rs.getDouble("kontrol"))
            .loadedQty(rs.getDouble("zagrujeno"))
            .calcQtyLane(rs.getDouble("STDCUBE") / nSqlValue)
            .build();



    private static final RowMapper<OrderDetailDto> ORDER_DETAIL_MAPPER = (rs, rowNum) ->
            OrderDetailDto.builder()
                    .sku(rs.getString("SKU"))
                    .descr(rs.getString("DESCR"))
                    .orderLineNumber(rs.getString("LINE_NUMBER"))
                    .packKey(rs.getString("PACKKEY"))
                    .openQty(rs.getBigDecimal("OPENQTY"))
                    .qtyAllocated(rs.getBigDecimal("QTYALLOCATED"))
                    .qtyPicked(rs.getBigDecimal("QTYPICKED"))
                    .shippedQty(rs.getBigDecimal("SHIPPEDQTY"))
                    .qtyLeft(rs.getBigDecimal("UN_Allocated"))
                    .build();

    private static final RowMapper<String> WAVE_KEY_MAPPER = (rs, rowNum) -> rs.getString("WAVEKEY");


    public List<OrderLineDto> getOrderLines(String externalLoadId){
        MapSqlParameterSource params = new MapSqlParameterSource("externalLoadId", externalLoadId);
        List<OrderLineDto> orderLines = namedParameterJdbcTemplate.query(SELECT_ORDER_DETAIL_LIST, params, ORDERS_DETAIL_MAPPER);
        return orderLines;
    }

    public List<OrderDto> getOrderList(String externalLoadId){

        this.nSqlValue = Double.parseDouble(nSqlConfigDao.getNSqlConfig(CONFIGKEY).getNSqlValue());
        List<OrderLineDto> orderLines = getOrderLines(externalLoadId);

        List<OrderDto> theResult = new ArrayList<>();
        Map<String, List<OrderLineDto>> groupedByRouteOrderLines = orderLines.stream()
                .collect(Collectors.groupingBy(OrderLineDto::getRoute));

        groupedByRouteOrderLines.entrySet().stream().forEach( item ->{
            OrderDto orderDto = new OrderDto();
            orderDto.setRoute(item.getKey());
            orderDto.setDetails(item.getValue());
            orderDto.setOrdersCount(orderDto.getDetails().size());
            orderDto.setStop(orderDto.getDetails().stream()
                    .map(OrderLineDto::getStop)
                    .distinct().findAny().orElse(0));

            orderDto.setTotalCalcQtyLane(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getCalcQtyLane)
                    .sum()
            );
            orderDto.setTotalControlled(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getControlledQty)
                    .sum()/ orderDto.getDetails().size()
            );
            orderDto.setTotalLoaded(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getLoadedQty)
                    .sum()/ orderDto.getDetails().size()
            );
            orderDto.setTotalPacked(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getPackedQty)
                    .sum()/ orderDto.getDetails().size()
            );
            orderDto.setTotalPicked(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getPickedQty)
                    .sum()/ orderDto.getDetails().size()
            );
            orderDto.setTotalSelectedCartonIdQty(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getSelectedCartonIdQty)
                    .sum()
            );
            orderDto.setTotalStdCube(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getStdCube)
                    .sum()
            );
            orderDto.setTotalStdGrossWgt(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getStdGrossWgt)
                    .sum()
            );
            orderDto.setTotalLeftToControlQty(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getLeftToControl)
                    .sum()
            );
            orderDto.setTotalLeftToPickQty(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getLeftToPick)
                    .sum()
            );
            orderDto.setTotalOrderLines(orderDto.getDetails().stream()
                    .mapToDouble(OrderLineDto::getOrderLines)
                    .sum()
            );
            theResult.add(orderDto);
        });
       return theResult;
    }


    public List<OrderDetailDto> getOrderDetail(String orderKey){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(ORDER_DETAIL_SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource("orderKey", orderKey);
        List<OrderDetailDto> orderDetailList = (List<OrderDetailDto>)
                simpleJdbcCall.returningResultSet(ORDER_DETAIL_MAP_KEY_NAME, ORDER_DETAIL_MAPPER)
                        .execute(params).get(ORDER_DETAIL_MAP_KEY_NAME);
        return orderDetailList;
    }


    public List<String> getWaveKeys(String orderKey, String loginId, int operationType){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(WAVES_SQL_PROCEDURE_NAME)
                .withSchemaName(SCHEMA_NAME);
        MapSqlParameterSource params = new MapSqlParameterSource("externalid", orderKey)
                    .addValue("user_login",loginId)
                    .addValue("oper_type", operationType);

        List<String> waveKeys  = ((List<String>)simpleJdbcCall.returningResultSet(WAVE_MAP_KEY_NAME, WAVE_KEY_MAPPER)
                .execute(params).get(WAVE_MAP_KEY_NAME));
        return waveKeys;
    }


    public List<String> getOrdersByExternalLoadId(String externalLoadId) {
        MapSqlParameterSource params = new MapSqlParameterSource("externalLoadId", externalLoadId);
        return namedParameterJdbcTemplate.query(SELECT_ORDERS, params, (resultSet, i) -> resultSet.getString("ORDERKEY"));
    }

    public boolean isOrderAdministrativeDelivery(String orderKey) {
        MapSqlParameterSource params = new MapSqlParameterSource("orderKey", orderKey);
        return namedParameterJdbcTemplate.queryForObject(SELECT_ADMIN_DELIVERY, params, Integer.class) == 0;
    }

    public void updateOrderStatus(String orderKey, String shipStatus) {
        MapSqlParameterSource params = new MapSqlParameterSource("orderKey", orderKey)
                .addValue("status", shipStatus)
                .addValue("userKey", userDao.getUser().getLoginId());
        namedParameterJdbcTemplate.update(UPDATE_ORDER_STATUS, params);
    }

    public String getLoadIdByExternalLoadId(String externalLoadId) {
        MapSqlParameterSource params = new MapSqlParameterSource("extLoadId", externalLoadId);
        List<String> loadid = namedParameterJdbcTemplate.query(SELECT_LOADID, params, (resultSet, i) -> resultSet.getString("LOADID"));
        return  loadid.isEmpty() ? "" : loadid.get(0);
    }
}
