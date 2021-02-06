package ru.ltmanagement.routeplan.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class HeaderTableDto {
    private int planedTasks;
    private int realTasks;
    private int remindedTasks;
    private BigDecimal planedWeight;
    private BigDecimal realWeight;
    private BigDecimal remindedWeight;
    private BigDecimal planedCube;
    private BigDecimal realCube;
    private BigDecimal remindedCube;

}
