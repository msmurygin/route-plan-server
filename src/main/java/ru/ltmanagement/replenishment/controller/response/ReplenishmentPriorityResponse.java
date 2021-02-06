package ru.ltmanagement.replenishment.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.replenishment.dto.ReplenishmentPriorityDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ReplenishmentPriorityResponse {
    List<ReplenishmentPriorityDto> replenishmentPriorities;
}
