package ru.ltmanagement.routeplan.service.api;

import ru.ltmanagement.routeplan.dto.PlanRouteConfigurationDto;
import ru.ltmanagement.settings.dto.SettingsDto;

public interface ConfigurationService {

    PlanRouteConfigurationDto getConfiguration();

    void save(SettingsDto settingsDto);
}
