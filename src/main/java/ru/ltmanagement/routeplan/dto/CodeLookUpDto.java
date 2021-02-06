package ru.ltmanagement.routeplan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeLookUpDto {
    private String code;
    private String description;
}
