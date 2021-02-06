package ru.ltmanagement.settings.transformer;


import org.mapstruct.Mapper;
import ru.ltmanagement.settings.controller.request.SettingsRequest;
import ru.ltmanagement.settings.dto.SettingsDto;

@Mapper(componentModel = "spring")
public interface SettingsTransformer {


    SettingsDto transformFormRequestToDto(SettingsRequest request);
}
