package ru.ltmanagement.routeplan.service.api;

import ru.ltmanagement.routeplan.dto.SkuStockResponseDto;

public interface SkuStockService {
    SkuStockResponseDto getSkuStock(String sku);
}
