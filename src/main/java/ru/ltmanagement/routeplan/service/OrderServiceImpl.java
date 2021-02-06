package ru.ltmanagement.routeplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.ordermanagement.dao.OrderDao;
import ru.ltmanagement.ordermanagement.dto.OrderDetailDto;
import ru.ltmanagement.ordermanagement.dto.OrderDto;
import ru.ltmanagement.routeplan.service.api.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public List<OrderDto> getPlanRouteOrderList(String externalLoadId) {
        return  orderDao.getOrderList(externalLoadId);
    }

    @Override
    public List<OrderDetailDto> getOrderDetail(String orderKey) {
        return orderDao.getOrderDetail(orderKey);
    }
}
