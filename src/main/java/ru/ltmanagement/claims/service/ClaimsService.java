package ru.ltmanagement.claims.service;

import ru.ltmanagement.claims.controller.response.ClaimsDetailResponse;
import ru.ltmanagement.claims.controller.response.ClaimsResponse;
import ru.ltmanagement.claims.controller.response.ClaimsSkuDetailResponse;
import ru.ltmanagement.claims.dto.ClaimsRequestDto;

import java.util.List;

public interface ClaimsService {

    List<String> getSkus();

    ClaimsResponse getClaims(ClaimsRequestDto requestDto);

    ClaimsDetailResponse getClaimsDetail(String claimsNumber);

    ClaimsSkuDetailResponse getClaimsSkuDetail(String claimsNumber, String sku) ;

    void createTask(String claimsNumber, String sku);

    void sendToHost(String claimsNumber);
}
