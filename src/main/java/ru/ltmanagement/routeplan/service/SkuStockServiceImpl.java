package ru.ltmanagement.routeplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.routeplan.dao.SkuStockDao;
import ru.ltmanagement.routeplan.dto.SkuStockDto;
import ru.ltmanagement.routeplan.dto.SkuStockResponseDto;
import ru.ltmanagement.routeplan.service.api.SkuStockService;

import java.util.List;

@Service
public class SkuStockServiceImpl implements SkuStockService {

    @Autowired
    SkuStockDao skuStockDao;

    @Override
    public SkuStockResponseDto getSkuStock(String sku) {
        List<SkuStockDto> skuStock = skuStockDao.getSkuStock(sku);
        return new SkuStockResponseDto(skuStock);
    }
}
