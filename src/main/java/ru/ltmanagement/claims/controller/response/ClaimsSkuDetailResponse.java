package ru.ltmanagement.claims.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.claims.dto.ClaimsSkuDetailDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ClaimsSkuDetailResponse {
    private List<ClaimsSkuDetailDto> claimsSkuDetail;
}
