package ru.ltmanagement.ordermanagement.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ltmanagement.exceptions.IllegalArgumentsException;
import ru.ltmanagement.ordermanagement.controller.response.ReportResponse;
import ru.ltmanagement.ordermanagement.dao.OrderDao;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;
import ru.ltmanagement.user.dao.UserDao;

@Service
public class ReportServiceImpl implements ReportService {

    private static final String URL = "frameset?__report=report/"+"%s"+"&p_DATABASE=SCPRD&p_SCHEMA=WMWHSE1&__locale=ru_RU&__format="+"%s"+"&p_uid=%s&"+"%s"+"=%s";
    private static final String INVALID_CONTROLLER_PARAMS = "Не корретный параметр контроллера ";
    private static final String MANDATORY_FIELDS_EXCEPTION = "Для формирования отчета, отсутствуют обязательный параметр externalLoadId";

    @Value("${infor.report.server.url}")
    private String reportServer;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Override
    public ReportResponse getReportUrl(OrderMgrDto orderMgrDto, String reportName, String format, String paramName) {
        if (StringUtils.isEmpty(reportName) || StringUtils.isEmpty(format) || StringUtils.isEmpty(paramName))
            throw new IllegalArgumentsException(INVALID_CONTROLLER_PARAMS );

        if (StringUtils.isNoneEmpty(orderMgrDto.getExternalLoadId())){
            String loadId = orderMgrDto.getExternalLoadId().equalsIgnoreCase(orderMgrDto.getLoadUsr2()) ?
                    orderMgrDto.getExternalLoadId() : orderDao.getLoadIdByExternalLoadId(orderMgrDto.getExternalLoadId());
            if (StringUtils.isEmpty(loadId))
                throw new IllegalArgumentsException(INVALID_CONTROLLER_PARAMS + orderMgrDto.toString());

            String userLogin = userDao.getUser().getLoginId();
            return new ReportResponse(String.format(reportServer + URL, reportName, format, userLogin, paramName, loadId));
        }
        throw new IllegalArgumentsException(MANDATORY_FIELDS_EXCEPTION);
    }
}
