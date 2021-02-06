package ru.ltmanagement.routeplan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoutePlanRequestDto {
    private List<String> orderType;
    private List<String> destination;
    private String startPeriod;
    private String endPeriod;
    private boolean byShifts;
    private List<String> orderStatus;
}
