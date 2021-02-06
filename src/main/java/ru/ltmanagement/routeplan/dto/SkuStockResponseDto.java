package ru.ltmanagement.routeplan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SkuStockResponseDto {
    private double qtyAllocatedTotal;
    private double qtyTotal;
    private double qtyPickedTotal;
    private double qtyAvailable;
    private List<SkuStockDto> skuStock;

    public SkuStockResponseDto(List<SkuStockDto> skuStock){
        this.skuStock = skuStock;
        qtyAllocatedTotal = skuStock.stream().mapToDouble(o -> o.getQTYALLOCATED().doubleValue()).sum();
        qtyTotal = skuStock.stream().mapToDouble(o -> o.getQTY().doubleValue() ).sum();
        qtyPickedTotal = skuStock.stream().mapToDouble(o -> o.getQTYPICKED().doubleValue() ).sum();
        qtyAvailable  = skuStock.stream().mapToDouble(o -> o.getQTY_BALANCE().doubleValue() ).sum();
    }
}
