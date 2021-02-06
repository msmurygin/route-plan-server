package ru.ltmanagement.ordermanagement.service;

import ru.ltmanagement.ordermanagement.controller.response.OrderManagementResponse;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;

public interface OrderAllocationService {
    OrderManagementResponse release(OrderMgrDto orderMgrDto);
    OrderManagementResponse allocate(OrderMgrDto orderMgrDto);
}
