package ru.ltmanagement.claims.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaimsSkuDetailRequest {
    private String claimsNumber;
    private String sku;
}
