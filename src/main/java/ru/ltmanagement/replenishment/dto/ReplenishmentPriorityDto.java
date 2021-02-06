package ru.ltmanagement.replenishment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplenishmentPriorityDto {
    
    private Integer serialKey;

    private String locationType;
    
    private String sku;

    private String descr;

    private String loc;

    private BigDecimal qty;
    
    private BigDecimal replenishmentQty;
    
    private String priority;
    
    private String putawayZone;
    
    private String bypassRoute;
}
