package ru.ltmanagement.claims.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class ClaimDto {
    private String id;
    private String externalLoadId;
    private String client;
    private String addDate;
    private String dateOfArrival;
    private String linesCount;
    private String linesCountChecked;
    private String cost;
    private int taskState;
    private Date start;

}
