package ru.ltmanagement.ordermanagement.service;

import ru.ltmanagement.ordermanagement.controller.response.OrderManagementResponse;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;

public interface OrderShipService {
    OrderManagementResponse ship(OrderMgrDto orderMgrDto);
}
