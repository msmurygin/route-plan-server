package ru.ltmanagement.routeplan.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.ordermanagement.dto.OrderDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
   private List<OrderDto> orderList;
}
