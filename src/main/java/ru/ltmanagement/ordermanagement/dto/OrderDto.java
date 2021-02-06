package ru.ltmanagement.ordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String route;
    private Integer stop;
    private int ordersCount;
    private Double totalOrderLines;
    private Double totalLeftToPickQty;
    private Double totalLeftToControlQty;
    private Double totalPicked;
    private Double totalControlled;
    private Double totalPacked;
    private Double totalLoaded;
    private Double totalCalcQtyLane;
    private Double totalStdCube;
    private Double totalStdGrossWgt;
    private Double totalSelectedCartonIdQty;
    private List<OrderLineDto> details;
}
