package ru.ltmanagement.replenishment.controller.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReplenishmentPriorityRequest {
    private String sku;
    private String loc;
    private String zone;
}
