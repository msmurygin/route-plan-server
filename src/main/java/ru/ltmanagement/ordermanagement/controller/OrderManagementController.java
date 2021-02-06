package ru.ltmanagement.ordermanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.configuration.ControllerURL;
import ru.ltmanagement.ordermanagement.controller.request.OrderMgrRequest;
import ru.ltmanagement.ordermanagement.controller.response.OrderManagementResponse;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;
import ru.ltmanagement.ordermanagement.service.OrderAllocationService;
import ru.ltmanagement.ordermanagement.service.OrderCloseService;
import ru.ltmanagement.ordermanagement.service.OrderCancelAllocationService;
import ru.ltmanagement.ordermanagement.service.OrderShipService;
import ru.ltmanagement.ordermanagement.transformer.OrderManagementTransformer;

@RestController
@Slf4j
public class OrderManagementController {

    @Autowired
    private OrderManagementTransformer transformer;

    @Autowired
    private OrderAllocationService orderAllocationService;

    @Autowired
    private OrderCloseService orderCloseService;

    @Autowired
    private OrderCancelAllocationService orderCancelAllocationService;

    @Autowired
    private OrderShipService orderShipService;

    @PostMapping(path = ControllerURL.RELEASE_ORDERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderManagementResponse> release(@RequestBody OrderMgrRequest request){
        OrderMgrDto orderMgrDto = transformer.transformOrderMgrRequestToDto(request);
        OrderManagementResponse releaseResult = orderAllocationService.release(orderMgrDto);
        return ResponseEntity.ok(releaseResult);
    }

    @PostMapping(path = ControllerURL.ALLOCATE_ORDERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderManagementResponse> allocate(@RequestBody OrderMgrRequest request){
        OrderMgrDto orderMgrDto = transformer.transformOrderMgrRequestToDto(request);
        OrderManagementResponse allocateResult = orderAllocationService.allocate(orderMgrDto);
        return ResponseEntity.ok(allocateResult);
    }

    @PostMapping(path = ControllerURL.CLOSE_ORDERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderManagementResponse> closeOrders(@RequestBody OrderMgrRequest request){
        OrderMgrDto orderMgrDto = transformer.transformOrderMgrRequestToDto(request);
        OrderManagementResponse closeResult = orderCloseService.ordersClose(orderMgrDto);
        return ResponseEntity.ok(closeResult);
    }

    @PostMapping(path = ControllerURL.CLOSE_ONE_ORDER_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderManagementResponse> closeOneOrder(@RequestBody OrderMgrRequest request){
        OrderMgrDto orderMgrDto = transformer.transformOrderMgrRequestToDto(request);
        OrderManagementResponse closeResult = orderCloseService.orderClose(orderMgrDto.getOrderKey());
        return ResponseEntity.ok(closeResult);
    }


    @PostMapping(path = ControllerURL.CANCEL_ORDERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderManagementResponse> cancel(@RequestBody OrderMgrRequest request){
        OrderMgrDto orderMgrDto = transformer.transformOrderMgrRequestToDto(request);
        OrderManagementResponse closeResult = orderCancelAllocationService.cancel(orderMgrDto);
        return ResponseEntity.ok(closeResult);
    }

    @PostMapping(path = ControllerURL.SHIP_ORDERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderManagementResponse> ship(@RequestBody OrderMgrRequest request){
        OrderMgrDto orderMgrDto = transformer.transformOrderMgrRequestToDto(request);
        OrderManagementResponse closeResult = orderShipService.ship(orderMgrDto);
        return ResponseEntity.ok(closeResult);
    }


}
