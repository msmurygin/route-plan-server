package ru.ltmanagement.routeplan.service.api;

import ru.ltmanagement.routeplan.controller.request.OrderListPutRequest;
import ru.ltmanagement.routeplan.controller.request.RoutePlanPutRequest;
import ru.ltmanagement.routeplan.controller.response.HeaderTableResponse;
import ru.ltmanagement.routeplan.dto.ActiveUserDto;
import ru.ltmanagement.routeplan.dto.RoutePlanRequestDto;

public interface RoutePlanService {

    void updateTable(RoutePlanPutRequest request);

    HeaderTableResponse getTable(RoutePlanRequestDto routePlanRequestDTO);

    ActiveUserDto getActiveUsers();

    void updateOrderList(OrderListPutRequest request);
}
