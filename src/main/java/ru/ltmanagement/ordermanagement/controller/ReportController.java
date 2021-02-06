package ru.ltmanagement.ordermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.configuration.ControllerURL;
import ru.ltmanagement.ordermanagement.controller.request.OrderMgrRequest;
import ru.ltmanagement.ordermanagement.controller.response.ReportResponse;
import ru.ltmanagement.ordermanagement.dto.OrderMgrDto;
import ru.ltmanagement.ordermanagement.service.ReportService;
import ru.ltmanagement.ordermanagement.transformer.OrderManagementTransformer;

@RestController
public class ReportController {

    @Autowired
    private OrderManagementTransformer transformer;

    @Autowired
    private ReportService reportService;

    @PostMapping(path = ControllerURL.REPORTS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportResponse> pickList(@RequestBody OrderMgrRequest request, @RequestParam String reportName, @RequestParam String format, @RequestParam String paramName){
        OrderMgrDto orderMgrDto = transformer.transformOrderMgrRequestToDto(request);
        ReportResponse pickListResponse = reportService.getReportUrl(orderMgrDto, reportName, format, paramName);
        return ResponseEntity.ok(pickListResponse);
    }
}
