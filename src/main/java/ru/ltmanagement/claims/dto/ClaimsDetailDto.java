package ru.ltmanagement.claims.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class ClaimsDetailDto {
    private int serialKey;
    private String externOrderKey;
    private String sku;
    private String descr;
    private BigDecimal qtyPicked;
    private BigDecimal qtyFact;
    private BigDecimal sumPrice;
    private String viewClaims;
    private String commentIn;
    private String whoseFault;
    private String reason;
    private String response;
    private String guiltyPick;
    private String guiltyStoreKeeper;
    private String guiltyController;
    private String auditor;
    private String mayChange;
}
