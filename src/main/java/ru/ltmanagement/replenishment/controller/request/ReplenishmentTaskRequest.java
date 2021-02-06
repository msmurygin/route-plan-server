package ru.ltmanagement.replenishment.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReplenishmentTaskRequest {
    private String externLoadId;
    private String orderKey;
    private boolean changePriority;
    private int priorityValue;
}
