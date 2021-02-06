package ru.ltmanagement.routeplan.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.ordermanagement.dto.OrderDetailDto;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class OrderDetailResponse {
    private List<OrderDetailDto> orderDetail;
}
