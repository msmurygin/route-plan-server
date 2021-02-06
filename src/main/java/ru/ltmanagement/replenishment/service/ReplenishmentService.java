package ru.ltmanagement.replenishment.service;

import ru.ltmanagement.replenishment.dto.ReplenishmentPriorityDto;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskDto;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskRequestDto;

import java.util.List;

public interface ReplenishmentService {
    List<ReplenishmentTaskDto> getReplenishmentTask(ReplenishmentTaskRequestDto replRequestDto);

    List<ReplenishmentPriorityDto> getReplenishmentPriority(String sku, String loc, String zone);

    void updateReplenishmentPriority(List<ReplenishmentPriorityDto> replenishmentUpdate);
}
