package ru.ltmanagement.ordermanagement.service;

import ru.ltmanagement.ordermanagement.controller.response.ReportResponse;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;

public interface ReportService {
    ReportResponse getReportUrl(OrderMgrDto orderMgrDto, String reportName, String format, String paramName);
}
