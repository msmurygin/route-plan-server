package ru.ltmanagement.ordermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.common.service.infor.InforClientService;
import ru.ltmanagement.ordermanagement.controller.response.OrderManagementResponse;
import ru.ltmanagement.ordermanagement.dao.OrderDao;
import ru.ltmanagement.ordermanagement.dao.OrderManagementDao;
import ru.ltmanagement.ordermanagement.dto.AllocationProcessingTypeResultDto;
import ru.ltmanagement.ordermanagement.dto.OrderLineDto;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;
import ru.ltmanagement.user.dao.UserDao;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.ltmanagement.ordermanagement.dao.OrderManagementDao.ACTION_SHIP;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.checkIfOrderClosed;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.isOrderSelfDelivery;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.response;

@Service
public class OrderShipServiceImpl implements OrderShipService {

    private static final String NULL = null;
    private static final String SHIP_HEADER = "Отгрузка заказа" ;
    private static final String SHIP_MESSAGE = "Заказ %s отгружен";
    private static final String MULTIPLE_ORDERS_SHIPPING_DONE_MESSAGE = "Заказы %s отгруженны";
    private static final String SHIP_IN_QUEUE_MESSAGE ="Заказ %s поставлен в очередь на отгрузку";
    private static final String SHIP_STATUS =  "95";

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderManagementDao orderMgrDao;

    @Autowired
    private InforClientService inforClientService;


    @Override
    public OrderManagementResponse ship(OrderMgrDto orderMgrDto) {
        String loginId =  userDao.getUser().getLoginId();
        if (isOrderSelfDelivery(orderMgrDto)){
            OrderLineDto orderLine = orderDao.getOrderLines(orderMgrDto.getExternalLoadId()).get(0);
            checkIfOrderClosed(orderLine);
            AllocationProcessingTypeResultDto allocProcessTypeResult = orderMgrDao.getAllocationProcessingType(NULL, orderLine.getOrderKey(), loginId, ACTION_SHIP);
            if (allocProcessTypeResult == AllocationProcessingTypeResultDto.SYNC_OPERATION){
                inforClientService.ship(orderLine.getOrderKey());
                processAdministrativeDelivery(orderLine.getOrderKey());
                return response(orderLine.getOrderKey(), SHIP_HEADER, SHIP_MESSAGE);
            }else{
                return response(orderLine.getOrderKey(), SHIP_HEADER, SHIP_IN_QUEUE_MESSAGE);
            }
        }
        AllocationProcessingTypeResultDto allocProcTypeResult = getAllocationProcessingTypeResultDto(orderMgrDto,  loginId, ACTION_SHIP);
        if (allocProcTypeResult == AllocationProcessingTypeResultDto.SYNC_OPERATION){
            List<String> orderKeys = Objects.isNull(orderMgrDto.getOrderKey()) ?
                    orderDao.getOrdersByExternalLoadId(orderMgrDto.getExternalLoadId()) :
                    Arrays.asList(orderMgrDto.getOrderKey());

            orderKeys.forEach(orderKey -> inforClientService.ship(orderKey));
            return createMultipleOrderShippingResultMessage(orderKeys);
        }else{
            return response(
                    Objects.isNull(orderMgrDto.getLoadUsr2()) ?
                            orderMgrDto.getOrderKey() :
                            orderMgrDto.getLoadUsr2(),
                    SHIP_HEADER, SHIP_IN_QUEUE_MESSAGE
            );
        }
    }

    private AllocationProcessingTypeResultDto getAllocationProcessingTypeResultDto(OrderMgrDto orderMgrDto, String loginId, int action) {
        AllocationProcessingTypeResultDto actionResultDto;
        if (Objects.isNull(orderMgrDto.getOrderKey()))
            actionResultDto = orderMgrDao.getAllocationProcessingType(orderMgrDto.getExternalLoadId(), NULL, loginId, action);
        else
            actionResultDto = orderMgrDao.getAllocationProcessingType(NULL, orderMgrDto.getOrderKey(), loginId, action);
        return actionResultDto;
    }


    private OrderManagementResponse createMultipleOrderShippingResultMessage(List<String> shipped) {
        return  OrderManagementResponse.builder()
                .header(SHIP_HEADER)
                .message(String.format(
                        MULTIPLE_ORDERS_SHIPPING_DONE_MESSAGE,
                        shipped.stream().collect(Collectors.joining(", "))
                )).build();
    }

    private void processAdministrativeDelivery(String orderKey) {
        if (orderDao.isOrderAdministrativeDelivery(orderKey)) {
            orderDao.updateOrderStatus(orderKey, SHIP_STATUS);
        }
    }
}
