package ru.ltmanagement.routeplan.service.api;

import ru.ltmanagement.ordermanagement.dto.OrderDetailDto;
import ru.ltmanagement.ordermanagement.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getPlanRouteOrderList(String externalLoadId);
    List<OrderDetailDto> getOrderDetail(String orderKey);
}
