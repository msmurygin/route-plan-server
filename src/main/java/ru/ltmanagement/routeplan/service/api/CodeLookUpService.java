package ru.ltmanagement.routeplan.service.api;

import ru.ltmanagement.routeplan.dto.CodeLookUpDto;

import java.util.List;

public interface CodeLookUpService {
    List<CodeLookUpDto> getCodeLookUp(String listName);
    List<String> getOrderRoutes();
    List<CodeLookUpDto> getOrderStatus();
}
