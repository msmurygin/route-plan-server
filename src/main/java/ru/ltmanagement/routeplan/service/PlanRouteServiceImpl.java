package ru.ltmanagement.routeplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ltmanagement.routeplan.controller.request.OrderListPutRequest;
import ru.ltmanagement.routeplan.controller.request.RoutePlanPutRequest;
import ru.ltmanagement.routeplan.controller.response.HeaderTableResponse;
import ru.ltmanagement.routeplan.dao.PlanRouteTableDao;
import ru.ltmanagement.routeplan.dto.ActiveUserDto;
import ru.ltmanagement.routeplan.dto.DetailTableDto;
import ru.ltmanagement.routeplan.dto.HeaderTableDto;
import ru.ltmanagement.routeplan.dto.PlanRouteConfigurationDto;
import ru.ltmanagement.routeplan.dto.RoutePlanRequestDto;
import ru.ltmanagement.routeplan.service.api.ConfigurationService;
import ru.ltmanagement.routeplan.service.api.RoutePlanService;
import ru.ltmanagement.routeplan.transofrmer.RoutePlanRequestTransformer;
import ru.ltmanagement.user.dao.ActiveUsersDao;
import ru.ltmanagement.user.dao.UserDao;
import ru.ltmanagement.user.dto.UserDto;

import java.util.List;

@Service
public class PlanRouteServiceImpl implements RoutePlanService {

    @Autowired
    private PlanRouteTableDao planRouteTableDAO;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ActiveUsersDao activeUsersDao;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private RoutePlanRequestTransformer transformer;



    @Override
    public HeaderTableResponse getTable(RoutePlanRequestDto routePlanRequestDTO) {
        PlanRouteConfigurationDto configDTO = configurationService.getConfiguration();
        UserDto user = userDao.getUser();
        HeaderTableDto headerTable = planRouteTableDAO.getHeaderTable(routePlanRequestDTO, configDTO, user);
        List<DetailTableDto> detailTable = planRouteTableDAO.getDetailTable(routePlanRequestDTO, configDTO, user);
        return transformer.headerTableDtoToResponse(headerTable, detailTable);
    }

    @Override
    public ActiveUserDto getActiveUsers() {
        return activeUsersDao.getActiveUsers();
    }

    @Override
    @Transactional
    public void updateOrderList(OrderListPutRequest request) {
        planRouteTableDAO.updateOrderList(request.getOrderList());
    }


    @Override
    @Transactional
    public void updateTable(RoutePlanPutRequest request) {
        planRouteTableDAO.updateTable(request.getDetails());
    }
}
