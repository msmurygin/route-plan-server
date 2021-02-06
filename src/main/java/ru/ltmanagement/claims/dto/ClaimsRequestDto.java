package ru.ltmanagement.claims.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ClaimsRequestDto {

    private String startDate;
    private String endDate;
    private Integer allDates;
    private String client;
    private Integer showCheck;
    private Integer showSend;
    private Integer showAll;
    private List<SkuFilterDto> skus;
    private String guilty;


}
