package ru.ltmanagement.routeplan.transofrmer;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ltmanagement.routeplan.controller.request.RoutePlanRequest;
import ru.ltmanagement.routeplan.controller.response.ActiveUserResponse;
import ru.ltmanagement.routeplan.controller.response.HeaderTableResponse;
import ru.ltmanagement.routeplan.controller.response.PlanRouteConfigurationResponse;
import ru.ltmanagement.routeplan.controller.response.SkuStockResponse;
import ru.ltmanagement.routeplan.dto.ActiveUserDto;
import ru.ltmanagement.routeplan.dto.DetailTableDto;
import ru.ltmanagement.routeplan.dto.HeaderTableDto;
import ru.ltmanagement.routeplan.dto.PlanRouteConfigurationDto;
import ru.ltmanagement.routeplan.dto.RoutePlanRequestDto;
import ru.ltmanagement.routeplan.dto.SkuStockResponseDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoutePlanRequestTransformer {

    RoutePlanRequestDto routePlanRequestToDTO(RoutePlanRequest routePlanRequest);

    @Mapping(source = "detailTable", target = "details")
    HeaderTableResponse headerTableDtoToResponse(HeaderTableDto headerTableDTO, List<DetailTableDto> detailTable );

    ActiveUserResponse activeUserDtoToResponse(ActiveUserDto activeUserDto);

    PlanRouteConfigurationResponse planRouteConfigurationDtoToResponse(PlanRouteConfigurationDto configuration);

    SkuStockResponse skuStockResponseDtoToSkuStockResponse(SkuStockResponseDto skuStock);

    default List<String> mapStringToList(String value){
        if (StringUtils.isNoneEmpty(value))
            return Arrays.asList(value.split(","));
        return Collections.emptyList();
    }
}
