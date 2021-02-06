package ru.ltmanagement.claims.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class ClaimsSkuDetailDto {
    private String id;
    private String sku;
    private String descr;
    private String loc;
    private BigDecimal qty;
    private BigDecimal tdQty;
    private String result;
    private String tdStatus;
    private String tdDate;
    private String auditor;
    private String reason;
}
