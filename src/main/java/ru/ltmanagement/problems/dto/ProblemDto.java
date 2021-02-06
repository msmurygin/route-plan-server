package ru.ltmanagement.problems.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProblemDto {
    private int rowNumber;//просто порядковый номер
    private String externalOrderKey;//заказ (название заказа)
    private String sku;//код товара
    private String descr;//описание товара
    private BigDecimal openQty;//заказанное кол-во
    private String reasonMessage;//причина нерезервирования
    private int reasonCode;//код причины
}
