package ru.ltmanagement.replenishment.transformer;

import org.mapstruct.Mapper;
import ru.ltmanagement.replenishment.controller.request.ReplenishmentTaskRequest;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskRequestDto;

@Mapper(componentModel = "spring")
public interface ReplenishmentTaskTransformer {
    ReplenishmentTaskRequestDto fromReplenishmentTaskRequestToDto(ReplenishmentTaskRequest request);
}
