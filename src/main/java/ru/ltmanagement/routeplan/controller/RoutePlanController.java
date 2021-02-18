package ru.ltmanagement.routeplan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.configuration.ControllerURL;
import ru.ltmanagement.ordermanagement.dto.OrderDetailDto;
import ru.ltmanagement.routeplan.controller.request.LocationRequest;
import ru.ltmanagement.routeplan.controller.request.OrderListPutRequest;
import ru.ltmanagement.routeplan.controller.request.RoutePlanPutRequest;
import ru.ltmanagement.routeplan.controller.request.RoutePlanPutResponse;
import ru.ltmanagement.routeplan.controller.request.RoutePlanRequest;
import ru.ltmanagement.routeplan.controller.response.ActiveUserResponse;
import ru.ltmanagement.routeplan.controller.response.CodeLookUpResponse;
import ru.ltmanagement.routeplan.controller.response.HeaderTableResponse;
import ru.ltmanagement.routeplan.controller.response.LocationResponse;
import ru.ltmanagement.routeplan.controller.response.OrderDetailResponse;
import ru.ltmanagement.routeplan.controller.response.OrderResponse;
import ru.ltmanagement.routeplan.controller.response.RouteResponse;
import ru.ltmanagement.routeplan.controller.response.SkuStockResponse;
import ru.ltmanagement.routeplan.dto.ActiveUserDto;
import ru.ltmanagement.routeplan.dto.SkuStockResponseDto;
import ru.ltmanagement.routeplan.service.api.CodeLookUpService;
import ru.ltmanagement.routeplan.service.api.LocationService;
import ru.ltmanagement.routeplan.service.api.OrderService;
import ru.ltmanagement.routeplan.service.api.RoutePlanService;
import ru.ltmanagement.routeplan.service.api.SkuStockService;
import ru.ltmanagement.routeplan.transofrmer.RoutePlanRequestTransformer;

import java.util.List;
@CrossOrigin( origins = {"http://localhost:4200", "http://sekbtt-wmsapp01.myway.local:4200/" ,"http://sekbtt-wmsapp01.myway.local/"})
@RestController()
@Slf4j
public class RoutePlanController {

    @Autowired
    private RoutePlanService routePlanService;

    @Autowired
    private CodeLookUpService codeLookUpService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private SkuStockService skuStockService;

    @Autowired
    private RoutePlanRequestTransformer transformer;

    @Autowired
    private LocationService locationService;


    @PostMapping(path = ControllerURL.ROUTE_PLAN_HEADER_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HeaderTableResponse headerTable(@RequestBody RoutePlanRequest request){
        return routePlanService.getTable(transformer.routePlanRequestToDTO(request));
    }

    @GetMapping(path = ControllerURL.ROUTE_PLAN_CODE_LOOK_UP_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CodeLookUpResponse> getRoutePlanOrderType(@RequestParam String listName){
        return ResponseEntity.ok(new CodeLookUpResponse(codeLookUpService.getCodeLookUp(listName)));
    }

    @GetMapping(path = ControllerURL.ROUTE_PLAN_CODE_ROUTES_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RouteResponse getRoutePlanRoutes(){
        return new RouteResponse(codeLookUpService.getOrderRoutes());
    }


    @GetMapping(path = ControllerURL.ROUTE_PLAN_CODE_STATUS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeLookUpResponse getRoutePlanStatuses(){
        return new CodeLookUpResponse(codeLookUpService.getOrderStatus());
    }

    @GetMapping(path = ControllerURL.ROUTE_PLAN_ORDER_LIST_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse getOrderList(@RequestParam String externalLoadId){
        return new OrderResponse(orderService.getPlanRouteOrderList(externalLoadId));
    }


    @GetMapping(path = ControllerURL.ROUTE_PLAN_ACTIVE_USERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ActiveUserResponse getActiveUsers(){
        ActiveUserDto activeUsers = routePlanService.getActiveUsers();
        return transformer.activeUserDtoToResponse(activeUsers);
    }
    @GetMapping(path = ControllerURL.ROUTE_PLAN_ORDER_DETAIL_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDetailResponse getOrderDetail(@RequestParam String orderKey){
        List<OrderDetailDto> orderDetail = orderService.getOrderDetail(orderKey);
        return new OrderDetailResponse(orderDetail);
    }

    @GetMapping(path = ControllerURL.ROUTE_PLAN_SKU_STOCK_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SkuStockResponse> getSkuStock(@RequestParam String sku){
        SkuStockResponseDto skuStock = skuStockService.getSkuStock(sku);
        return ResponseEntity.ok(transformer.skuStockResponseDtoToSkuStockResponse(skuStock));
    }

    @PostMapping(path = ControllerURL.ROUTE_PLAN_LOCATION_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationResponse> getLocations(@RequestBody LocationRequest request){
        List<String> locations = locationService.getLocations(request.getLocationType(), request.getLocationCategory());
        return ResponseEntity.ok(new LocationResponse(locations));
    }

    @PutMapping(path = ControllerURL.ROUTE_PLAN_HEADER_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateRoutePlanTableData(@RequestBody RoutePlanPutRequest request){
        routePlanService.updateTable(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = ControllerURL.ROUTE_PLAN_ORDER_LIST_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoutePlanPutResponse> updateOrderList(@RequestBody OrderListPutRequest request){
        routePlanService.updateOrderList(request);
        return ResponseEntity.ok(new RoutePlanPutResponse("OK"));
    }

}
