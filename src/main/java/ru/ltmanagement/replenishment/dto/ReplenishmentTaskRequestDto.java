package ru.ltmanagement.replenishment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplenishmentTaskRequestDto {
    private String externLoadId;
    private String orderKey;
    private boolean changePriority;
    private int priorityValue;
}
