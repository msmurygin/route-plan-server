package ru.ltmanagement.ordermanagement.transformer;

import org.mapstruct.Mapper;
import ru.ltmanagement.ordermanagement.controller.request.OrderMgrRequest;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;

@Mapper(componentModel = "spring")
public interface OrderManagementTransformer {

    OrderMgrDto transformOrderMgrRequestToDto(OrderMgrRequest request);

}

