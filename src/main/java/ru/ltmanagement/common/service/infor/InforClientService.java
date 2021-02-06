package ru.ltmanagement.common.service.infor;

import ru.ltmanagement.ordermanagement.dto.OrderCloseDto;
import ru.ltmanagement.security.controller.response.AuthResponse;

public interface InforClientService {
    AuthResponse login(String userId, String password);
    void release(String waveKey);
    void allocate(String orderKey);
    void unAllocate(String orderKey);
    void ship(String orderKey);
    void closeOrder(OrderCloseDto orderCloseDto);
    void createTask(String claimsNumber, String sku);
    void sendToHost(String claimsNumber);
}
