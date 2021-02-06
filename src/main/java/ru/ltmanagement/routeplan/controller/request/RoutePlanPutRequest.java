package ru.ltmanagement.routeplan.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ltmanagement.routeplan.dto.DetailTableDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoutePlanPutRequest implements Serializable {
    private List<DetailTableDto> details;
}
