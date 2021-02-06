package ru.ltmanagement.routeplan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListPutRequestDto {
    private Integer stop;
    private String orderKey;
    private String packingLocation;
    private String door;
    private LocalDateTime vehicleArrival;
    private LocalDateTime vehicleLeaving;
}
