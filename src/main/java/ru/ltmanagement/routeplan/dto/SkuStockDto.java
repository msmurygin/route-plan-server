package ru.ltmanagement.routeplan.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SkuStockDto {

    private String PUTAWAYZONE;//зона
    private String DESCR_ZONE;//описание зоны
    private String LOC;//ячейка
    private String DESCR_LOC;//описание типа ячейки
    private String STATUS;//статус позиции
    private BigDecimal QTY;//количество общее
    private BigDecimal QTYALLOCATED;//зарезервированное кол-во
    private BigDecimal QTYPICKED;//отобранное кол-во
    private BigDecimal QTY_BALANCE;//доступное кол-во

}
