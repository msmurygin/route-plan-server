package ru.ltmanagement.settings.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.configuration.ControllerURL;
import ru.ltmanagement.routeplan.controller.response.PlanRouteConfigurationResponse;
import ru.ltmanagement.routeplan.dto.PlanRouteConfigurationDto;
import ru.ltmanagement.routeplan.service.api.ConfigurationService;
import ru.ltmanagement.routeplan.transofrmer.RoutePlanRequestTransformer;
import ru.ltmanagement.settings.NSqlConfigDto;
import ru.ltmanagement.settings.controller.request.SettingsRequest;
import ru.ltmanagement.settings.controller.response.NSqlConfigResponse;
import ru.ltmanagement.settings.dto.SettingsDto;
import ru.ltmanagement.settings.service.NSqlConfigService;
import ru.ltmanagement.settings.transformer.SettingsTransformer;

@RestController
@Slf4j
public class SettingsController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private NSqlConfigService nSqlConfigService;

    @Autowired
    private RoutePlanRequestTransformer transformer;

    @Autowired
    private SettingsTransformer settingsTransformer;

    @GetMapping(path = ControllerURL.ROUTE_PLAN_CONFIG_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlanRouteConfigurationResponse getConfiguration(){
        PlanRouteConfigurationDto configuration = configurationService.getConfiguration();
        return transformer.planRouteConfigurationDtoToResponse(configuration);
    }

    @GetMapping(path = ControllerURL.N_SQL_CONFIG_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NSqlConfigResponse> getNSqlConfig(@RequestParam String configKey){
        NSqlConfigDto nSqlValue = nSqlConfigService.getNSqlValue(configKey);
        return ResponseEntity.ok(new NSqlConfigResponse(nSqlValue));
    }

    @PostMapping(path = ControllerURL.ROUTE_PLAN_CONFIG_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateSettings(@RequestBody SettingsRequest request) {
        SettingsDto settingsDto =  settingsTransformer.transformFormRequestToDto(request);
        configurationService.save(settingsDto);
        return ResponseEntity.ok().build();
    }
}
