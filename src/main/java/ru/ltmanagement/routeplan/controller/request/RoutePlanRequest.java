package ru.ltmanagement.routeplan.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoutePlanRequest {
    private String orderType;
    private String destination;
    private String startPeriod;
    private String endPeriod;
    private boolean byShifts;
    private String orderStatus;
}
