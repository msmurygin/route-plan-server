package ru.ltmanagement.routeplan.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ltmanagement.routeplan.dto.OrderListPutRequestDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListPutRequest {
    private List<OrderListPutRequestDto> orderList;
}
