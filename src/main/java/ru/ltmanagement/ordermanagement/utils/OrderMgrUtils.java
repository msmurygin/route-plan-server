package ru.ltmanagement.ordermanagement.utils;

import ru.ltmanagement.exceptions.OrderAlreadyClosedException;
import ru.ltmanagement.exceptions.OrderAlreadyPickedException;
import ru.ltmanagement.ordermanagement.controller.response.OrderManagementResponse;
import ru.ltmanagement.ordermanagement.dto.OrderLineDto;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;

import java.util.Objects;

public class OrderMgrUtils {
    private static final String ALREADY_CLOSE_MESSAGE = "Заказ %s закрыт!";
    private static final String ALREADY_PICKED_MESSAGE = "Выпуск, резервирование, отмена резервирования - запрещено!";

    public static boolean isOrderSelfDelivery(OrderMgrDto orderMgrDto) {
        return orderMgrDto.getOrderKey() == null && Objects.equals(orderMgrDto.getExternalLoadId(), orderMgrDto.getLoadUsr2()) ;
    }


    public static void chekRoutines(OrderLineDto orderLine) {
        checkIfOrderClosed(orderLine);
        checkIfOrderPicked(orderLine);
    }

    public static void checkIfOrderPicked(OrderLineDto orderLine) {
        if (orderLine.getPickedQty() >= 100){
            throw new OrderAlreadyPickedException(ALREADY_PICKED_MESSAGE);
        }
    }

    public static void checkIfOrderClosed(OrderLineDto orderLine) {
        if (orderLine.getOrderClosed() == 1){
            throw new OrderAlreadyClosedException(ALREADY_CLOSE_MESSAGE, orderLine.getExternalOrderKey2());
        }
    }


    public static OrderManagementResponse response(String key, String header, String msg) {
        return OrderManagementResponse.builder()
                .header(header)
                .message(String.format(msg, key))
                .build();
    }

}
