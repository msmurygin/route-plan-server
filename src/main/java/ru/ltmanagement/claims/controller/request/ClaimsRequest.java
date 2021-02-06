package ru.ltmanagement.claims.controller.request;

import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.claims.dto.SkuFilterDto;

import java.util.List;

@Setter
@Getter
public class ClaimsRequest {

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

