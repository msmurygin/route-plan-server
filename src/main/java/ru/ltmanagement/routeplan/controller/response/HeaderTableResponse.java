package ru.ltmanagement.routeplan.controller.response;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.routeplan.dto.DetailTableDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({ "planedTasks", "realTasks", "remindedTasks", "planedWeight", "realWeight" ,"remindedWeight","planedCube","realCube","remindedCube", "details"})
public class HeaderTableResponse {
    private int planedTasks;
    private int realTasks;
    private int remindedTasks;
    private BigDecimal planedWeight;
    private BigDecimal realWeight;
    private BigDecimal remindedWeight;
    private BigDecimal planedCube;
    private BigDecimal realCube;
    private BigDecimal remindedCube;
    private List<DetailTableDto> details;
}
