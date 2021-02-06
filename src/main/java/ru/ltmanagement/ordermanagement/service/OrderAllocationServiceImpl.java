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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.ltmanagement.ordermanagement.dao.OrderManagementDao.ACTION_ALLOCATE;
import static ru.ltmanagement.ordermanagement.dao.OrderManagementDao.ACTION_RELEASE;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.chekRoutines;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.isOrderSelfDelivery;
import static ru.ltmanagement.ordermanagement.utils.OrderMgrUtils.response;


@Service
public class OrderAllocationServiceImpl implements OrderAllocationService {

    private static final String NULL = null;
    private static final String SENT_TO_CUSTOMER_MESSAGE = "Дествие не выполнено т.к. по заказу %s уже отправлены необходимые данные в 1С";
    private static final String ALLOCATION_DONE_HEADER = "Резервирование выполнено";
    private static final String ALLOCATION_IN_QUEUE_HEADER = "Резервирование поставлено в очередь";
    private static final String ORDER_IN_QUEUE = "Заказ %s оставлен в очередь";
    private static final String MULTIPLE_ORDERS_ALLOCATED_DONE_MESSAGE = "Заказы %s зарезервированы, \n " +
                                                                        "кроме заказов: %s т.к. по ним сообщение в 1С уже отправлено";
    private static final String RELEASE_DONE_HEADER = "Выпуск выполнен";
    private static final String RELEASE_IN_QUEUE_HEADER = "Выпуск в очереди";
    private static final String ORDER_RELEASE_DONE = "Заказ %s выпущен";
    private static final String ORDER_ALLOCATION_DONE ="Заказ %s зарезервирован";

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
    public OrderManagementResponse release(OrderMgrDto orderMgrDto) {
        String loginId =  userDao.getUser().getLoginId();
        if (isOrderSelfDelivery(orderMgrDto)) {
            OrderLineDto orderLine = orderDao.getOrderLines(orderMgrDto.getExternalLoadId()).get(0);
            chekRoutines(orderLine);
            if (transmitLogDao.isOrderPickedSentToCustomerHost(orderLine.getOrderKey())){
                throw new OrderSentToCustomerException(SENT_TO_CUSTOMER_MESSAGE, orderLine.getOrderKey());
            }
            AllocationProcessingTypeResultDto allocProcessTypeResult = orderMgrDao.getAllocationProcessingType(NULL, orderLine.getOrderKey(), loginId, ACTION_RELEASE);
            return releaseOrPutInQueueBySomeId(orderMgrDto, loginId, allocProcessTypeResult);
        }
        AllocationProcessingTypeResultDto actionResultDto = getAllocationProcessingTypeResultDto(orderMgrDto, loginId, ACTION_RELEASE);

        return releaseOrPutInQueueBySomeId(orderMgrDto, loginId, actionResultDto);
    }

    @Override
    public OrderManagementResponse allocate(OrderMgrDto orderMgrDto) {
        String loginId =  userDao.getUser().getLoginId();
        if (isOrderSelfDelivery(orderMgrDto)){
            OrderLineDto orderLine = orderDao.getOrderLines(orderMgrDto.getExternalLoadId()).get(0);
            chekRoutines(orderLine);
            if (transmitLogDao.isOrderPickedSentToCustomerHost(orderLine.getOrderKey())){
                throw new OrderSentToCustomerException(SENT_TO_CUSTOMER_MESSAGE, orderLine.getOrderKey());
            }
            AllocationProcessingTypeResultDto allocProcessTypeResult = orderMgrDao.getAllocationProcessingType(NULL, orderLine.getOrderKey(), loginId, ACTION_ALLOCATE);
            if (allocProcessTypeResult == AllocationProcessingTypeResultDto.SYNC_OPERATION){
                inforClientService.allocate(orderLine.getOrderKey());
                return response(orderMgrDto.getExternalLoadId(), ALLOCATION_DONE_HEADER, ORDER_ALLOCATION_DONE);
            }else{
                return response(orderMgrDto.getExternalLoadId(), ALLOCATION_IN_QUEUE_HEADER, ORDER_IN_QUEUE);
            }
        }
        AllocationProcessingTypeResultDto actionResultDto = getAllocationProcessingTypeResultDto(orderMgrDto, loginId, ACTION_ALLOCATE);
        if (actionResultDto == AllocationProcessingTypeResultDto.SYNC_OPERATION){
            List<String> orderCandidates = Objects.isNull(orderMgrDto.getOrderKey()) ?
                    orderDao.getOrdersByExternalLoadId(orderMgrDto.getExternalLoadId()) :
                    Arrays.asList(orderMgrDto.getOrderKey());

            List<String> notAllocatedOrders = tryAllocateAllAndCreateNotAllocatedList(orderCandidates);
            orderCandidates.removeIf(orderKeySent -> notAllocatedOrders.contains(orderKeySent));
            return createMultipleOrderAllocationResultMessage(orderCandidates, notAllocatedOrders);
        }else{
            return response(
                    Objects.isNull(orderMgrDto.getLoadUsr2()) ?
                            orderMgrDto.getOrderKey() :
                            orderMgrDto.getLoadUsr2(),
                    ALLOCATION_IN_QUEUE_HEADER, ORDER_IN_QUEUE
            );
        }
    }


    private OrderManagementResponse releaseOrPutInQueueBySomeId(OrderMgrDto orderMgrDto, String loginId, AllocationProcessingTypeResultDto allocType) {
        String searchById ;
        int operationType = 1;
        if (Objects.isNull(orderMgrDto.getOrderKey())) {
            searchById = orderMgrDto.getExternalLoadId();
            operationType = 1;
        }else {
            searchById = orderMgrDto.getOrderKey();
            operationType  = 2;
        }
        if (allocType == AllocationProcessingTypeResultDto.SYNC_OPERATION){
            List<String> waveKeys = orderDao.getWaveKeys(searchById, loginId, operationType);
            waveKeys.forEach(inforClientService::release);
            return getOrderReleasedResponse(orderMgrDto);
        }else{
            return getOrderReleaseInQueueResponse(orderMgrDto);
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



    private List<String> tryAllocateAllAndCreateNotAllocatedList(List<String> orderKeysToAllocate) {
        List<String> notAllocatedOrders = new ArrayList<>();
        orderKeysToAllocate.forEach(orderKey -> {
            if (transmitLogDao.isOrderPickedSentToCustomerHost(orderKey)){
                notAllocatedOrders.add(orderKey);
            }else{
                inforClientService.allocate(orderKey);
            }
        });
        return notAllocatedOrders;
    }


    private OrderManagementResponse getOrderReleaseInQueueResponse(OrderMgrDto orderMgrDto) {
        return OrderManagementResponse.builder()
                .header(RELEASE_IN_QUEUE_HEADER)
                .message(String.format(ORDER_IN_QUEUE,
                        Objects.isNull(orderMgrDto.getOrderKey()) ? orderMgrDto.getExternalLoadId() :
                                                                    orderMgrDto.getOrderKey()
                )).build();
    }

    private OrderManagementResponse getOrderReleasedResponse(OrderMgrDto orderMgrDto) {
        return OrderManagementResponse.builder()
                .header(RELEASE_DONE_HEADER)
                .message(String.format(ORDER_RELEASE_DONE, orderMgrDto.getExternalLoadId()))
                .build();
    }

    private OrderManagementResponse createMultipleOrderAllocationResultMessage(List<String> allocated, List<String> notAllocated) {
        return OrderManagementResponse.builder()
                .header(ALLOCATION_DONE_HEADER)
                .message(String.format(MULTIPLE_ORDERS_ALLOCATED_DONE_MESSAGE,
                        allocated.stream().collect(Collectors.joining(", ")),
                        notAllocated.stream().collect(Collectors.joining(", "))
                )).build();
    }




}
