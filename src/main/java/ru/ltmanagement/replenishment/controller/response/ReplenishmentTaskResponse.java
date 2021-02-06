package ru.ltmanagement.replenishment.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskDto;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ReplenishmentTaskResponse {
    List<ReplenishmentTaskDto> replenishmentTasks;
}
