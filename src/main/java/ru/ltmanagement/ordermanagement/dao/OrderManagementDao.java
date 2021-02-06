package ru.ltmanagement.ordermanagement.dao;

import ru.ltmanagement.ordermanagement.dto.AllocationProcessingTypeResultDto;
import ru.ltmanagement.ordermanagement.dto.OrderCloseDto;

import java.util.List;

public interface OrderManagementDao {
    int  ACTION_SHIP = 0;
    int  ACTION_RELEASE = 1;
    int  ACTION_UN_ALLOCATE = 2;
    int  ACTION_ALLOCATE = 3;


    AllocationProcessingTypeResultDto getAllocationProcessingType(String externalLoadId, String orderKey, String userId, int action);

    List<String> getOrderKeyInProcess(String externalLoadId);

    List<String> getUserNamesPickInProcess(String externalLoadId);

    List<String> getUserListPickingOrder(String orderKey);


    List<OrderCloseDto> getOrdersForClose(String externalLoadId, String orderKey);
}
