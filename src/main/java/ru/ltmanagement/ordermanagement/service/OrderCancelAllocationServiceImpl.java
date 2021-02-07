package ru.ltmanagement.ordermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.common.dao.TransmitLogDao;
import ru.ltmanagement.common.service.infor.InforClientService;
import ru.ltmanagement.exceptions.OrderSentToCustomerException;
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

import static ru.ltmanagement.ordermanagement.dao.OrderManagementDao.ACTION_UN_ALLOCATE;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.chekRoutines;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.isOrderSelfDelivery;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.response;

@Service
public class OrderCancelAllocationServiceImpl implements OrderCancelAllocationService {

    private static final String SENT_TO_CUSTOMER_MESSAGE = "Дествие не выполнено т.к. по заказу %s уже отправлены необходимые данные в 1С";
    private static final String NULL = null;
    private static final String UN_ALLOCATE_HEADER = "Разрезервирование заказа" ;
    private static final String UN_ALLOCATE_MESSAGE = "Заказ %s разрезервирован";
    private static final String UN_ALLOCATE_IN_QUEUE_MESSAGE = "Заказ %s поставлен в очередь на разрезервирование.";
    private static final String MULTIPLE_ORDERS_UN_ALLOCATED_DONE_MESSAGE = "Заказы %s зарезервированы";
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TransmitLogDao transmitLogDao;

    @Autowired
    private OrderManagementDao orderMgrDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private InforClientService inforClientService;


    @Override
    public OrderManagementResponse cancel(OrderMgrDto orderMgrDto) {
        String loginId =  userDao.getUser().getLoginId();
        if (isOrderSelfDelivery(orderMgrDto)){
            OrderLineDto orderLine = orderDao.getOrderLines(orderMgrDto.getExternalLoadId()).get(0);
            chekRoutines(orderLine);
            if (transmitLogDao.isOrderPickedSentToCustomerHost(orderLine.getOrderKey())){
                throw new OrderSentToCustomerException(SENT_TO_CUSTOMER_MESSAGE, orderLine.getOrderKey());
            }
            AllocationProcessingTypeResultDto allocProcessTypeResult = orderMgrDao.getAllocationProcessingType(NULL, orderLine.getOrderKey(), loginId, ACTION_UN_ALLOCATE);
            if (allocProcessTypeResult == AllocationProcessingTypeResultDto.SYNC_OPERATION){
                inforClientService.unAllocate(orderLine.getOrderKey());
                return response(orderLine.getOrderKey(), UN_ALLOCATE_HEADER, UN_ALLOCATE_MESSAGE);
            }else{
                return  response(orderLine.getOrderKey(), UN_ALLOCATE_HEADER, UN_ALLOCATE_IN_QUEUE_MESSAGE);
            }
        }
        AllocationProcessingTypeResultDto allocProcTypeResult = getAllocationProcessingTypeResultDto(orderMgrDto, loginId, ACTION_UN_ALLOCATE);
        if (allocProcTypeResult == AllocationProcessingTypeResultDto.SYNC_OPERATION){

            List<String> orderKeys = Objects.isNull(orderMgrDto.getOrderKey()) ?
                    orderDao.getOrdersByExternalLoadId(orderMgrDto.getExternalLoadId()) :
                    Arrays.asList(orderMgrDto.getOrderKey());

            List<String> orders = orderKeys.stream()
                    .filter(orderKey -> !transmitLogDao.isOrderPickedSentToCustomerHost(orderKey))
                    .collect(Collectors.toList());

            orders.forEach(orderKey -> inforClientService.unAllocate(orderKey));
            return createMultipleOrderAllocationResultMessage(orderKeys);
        }else{
            return response(
                    Objects.isNull(orderMgrDto.getLoadUsr2()) ?
                            orderMgrDto.getOrderKey() :
                            orderMgrDto.getLoadUsr2(),
                    UN_ALLOCATE_HEADER, UN_ALLOCATE_IN_QUEUE_MESSAGE
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

    private OrderManagementResponse createMultipleOrderAllocationResultMessage(List<String> allocated) {
        return  OrderManagementResponse.builder()
                .header(UN_ALLOCATE_HEADER)
                .message(String.format(
                        MULTIPLE_ORDERS_UN_ALLOCATED_DONE_MESSAGE,
                        allocated.stream().collect(Collectors.joining(", "))
                )).build();
    }


}
