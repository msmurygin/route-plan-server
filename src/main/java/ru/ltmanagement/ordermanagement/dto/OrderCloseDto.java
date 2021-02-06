package ru.ltmanagement.ordermanagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class OrderCloseDto {
    private String orderKey;
    private int allowBackOrder;
    private String status;
    private int backOrderType;
    private String type;
    private int reasonCodeFlag;
    private int shipTogetherFlag;
    private int qtyPickInProcess;
    private int pickStatusFlag;
    private int mayCloseOrder;
}
