package ru.ltmanagement.ordermanagement.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMgrRequest {
    private String loadUsr2;
    private String externalLoadId;
    private String orderKey;
}
