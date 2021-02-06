package ru.ltmanagement.replenishment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplenishmentTaskDto {
    private String ROW_N;//просто порядковй номер
    private String SKU;//код товара
    private String DESCR;//описание товара
    private String LOC;//ячейка которая требует пополнения данного товара
    private String REPLENISHMENTPRIORITY;//приоритет пополнения
    private String PUTAWAYZONE;//зона в котором находится данная чейка
}
