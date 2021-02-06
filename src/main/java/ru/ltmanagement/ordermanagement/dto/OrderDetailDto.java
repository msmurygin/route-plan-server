package ru.ltmanagement.ordermanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailDto {
    private String orderLineNumber;//порядковй номер позиции в заказе
    private String sku;//код товара
    private String descr;//описание товара
    private BigDecimal openQty;//заказанное количество
    private BigDecimal qtyAllocated;//зарезервированное кол-во
    private BigDecimal qtyPicked;//отобранное кол-во
    private BigDecimal shippedQty;//отгруженное кол-во
    private BigDecimal qtyLeft;//сколько еще нужно
    private String packKey;//ключ упаковки
}
