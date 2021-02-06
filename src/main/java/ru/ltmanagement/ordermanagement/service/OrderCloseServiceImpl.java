package ru.ltmanagement.ordermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.common.service.infor.InforClientService;
import ru.ltmanagement.exceptions.OrderCanNotBeClosedException;
import ru.ltmanagement.exceptions.OrderInProcessException;
import ru.ltmanagement.exceptions.RouteCanNotBeClosedException;
import ru.ltmanagement.ordermanagement.controller.response.OrderManagementResponse;
import ru.ltmanagement.ordermanagement.dao.OrderManagementDao;
import ru.ltmanagement.ordermanagement.dto.OrderCloseDto;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderCloseServiceImpl implements OrderCloseService {
    private static final String ORDER_CAN_NOT_BE_CLOSED_EXCEPTION = "Не удалось закрыть заказ %s";
    private static final String ROUTE_CAN_NOT_BE_CLOSED_EXCEPTION = "Не удалось закрыть рейс %s";
    private static final String ORDER_IN_PROCESS_EXCEPTION = "Не удалось закрыть рейс %s\nДанный рейс отбирают:\n %s ,\n по заказам: %s ";
    private static final String ONE_ORDER_IN_PROCESS_EXCEPTION = "Не удалось закрыть заказ %s\nДанный рейс отбирают:\n %s ";
    private static final String ROUTE_CLOSE_HEADER = "Закрытие рейса";
    private static final String ORDER_CLOSE_HEADER = "Закрытие рейса";
    private static final String ROUTE_CLOSE_MESSAGE = "Рейс %s закрыт";
    private static final String ORDER_CLOSE_MESSAGE = "Заказ %s закрыт";
    @Autowired
    private OrderManagementDao orderMgrDao;

    @Autowired
    private InforClientService inforClientService;


    @Override
    public OrderManagementResponse orderClose(String orderKey) {
        List<String> userListPickingOrder = orderMgrDao.getUserListPickingOrder(orderKey);
        if (!userListPickingOrder.isEmpty()){
            throw new OrderInProcessException(
                    String.format(ONE_ORDER_IN_PROCESS_EXCEPTION, orderKey, userListPickingOrder.stream()
                            .collect(Collectors.joining(", "))
                    ));
        }

        List<OrderCloseDto> ordersForClose = orderMgrDao.getOrdersForClose(null, orderKey);
        Set<OrderCloseDto> ordersCanBeClosed = ordersForClose.stream()
                .filter(this::canClose)
                .collect(Collectors.toSet());

        if (ordersCanBeClosed.isEmpty()) {
            throw new OrderCanNotBeClosedException(getOrderCanNotBeClosedExceptionMessage(orderKey));
        }

        ordersForClose.forEach(order -> inforClientService.closeOrder(order));

        return OrderManagementResponse.builder()
                .header(ORDER_CLOSE_HEADER)
                .message(String.format(ORDER_CLOSE_MESSAGE, orderKey))
                .build();
    }


    @Override
    public OrderManagementResponse ordersClose(OrderMgrDto orderMgrDto) {

        List<String> selectorsUserNames = orderMgrDao.getUserNamesPickInProcess(orderMgrDto.getExternalLoadId());
        if (!selectorsUserNames.isEmpty()){
            List<String> orderKeyInProcess = orderMgrDao.getOrderKeyInProcess(orderMgrDto.getExternalLoadId());
            String orderKeys = orderKeyInProcess.stream().collect(Collectors.joining(", "));
            String selectors = selectorsUserNames.stream().collect(Collectors.joining(", "));
            throw new OrderInProcessException(
                    String.format(ORDER_IN_PROCESS_EXCEPTION, orderMgrDto.getLoadUsr2(), selectors, orderKeys)
            );
        }

        List<OrderCloseDto> ordersForClose = orderMgrDao.getOrdersForClose(
                orderMgrDto.getExternalLoadId().equalsIgnoreCase(orderMgrDto.getLoadUsr2()) ? null : orderMgrDto.getExternalLoadId(),
                orderMgrDto.getExternalLoadId().equalsIgnoreCase(orderMgrDto.getLoadUsr2()) ? orderMgrDto.getExternalLoadId() : null
        );

        Set<OrderCloseDto> ordersCanBeClosed = ordersForClose.stream()
                .filter(this::canClose)
                .collect(Collectors.toSet());
        if (ordersCanBeClosed.isEmpty()) {
            throw new RouteCanNotBeClosedException(getRouteCanNotBeClosedExceptionMessage(orderMgrDto.getLoadUsr2()));
        }
        ordersForClose.forEach(order -> inforClientService.closeOrder(order));

        return OrderManagementResponse.builder()
                .header(ROUTE_CLOSE_HEADER)
                .message(String.format(ROUTE_CLOSE_MESSAGE, orderMgrDto.getLoadUsr2()))
                .build();
    }



    private String getRouteCanNotBeClosedExceptionMessage(String route){
        return String.format(ROUTE_CAN_NOT_BE_CLOSED_EXCEPTION, route);
    }
    private String getOrderCanNotBeClosedExceptionMessage(String orderKey){
        return String.format(ORDER_CAN_NOT_BE_CLOSED_EXCEPTION, orderKey);
    }

    private boolean canClose(OrderCloseDto order) {
        return order.getShipTogetherFlag() == 0 && order.getPickStatusFlag() == 0   &&
                order.getQtyPickInProcess() == 0 && order.getReasonCodeFlag() ==0 &&
                !order.getType().equals("21") && order.getMayCloseOrder() == 1;
    }
}
