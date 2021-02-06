package ru.ltmanagement.claims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.claims.controller.response.ClaimsDetailResponse;
import ru.ltmanagement.claims.controller.response.ClaimsResponse;
import ru.ltmanagement.claims.controller.response.ClaimsSkuDetailResponse;
import ru.ltmanagement.claims.dao.ClaimsDao;
import ru.ltmanagement.claims.dto.ClaimDto;
import ru.ltmanagement.claims.dto.ClaimsRequestDto;
import ru.ltmanagement.claims.dto.ClaimsSkuDetailDto;
import ru.ltmanagement.common.service.infor.InforClientService;

import java.util.List;

@Service
public class ClaimsServiceImpl implements ClaimsService {

    @Autowired
    private ClaimsDao claimsDao;

    @Autowired
    private InforClientService inforClientService;

    @Override
    public List<String> getSkus() {
        return claimsDao.getSkus();
    }

    @Override
    public ClaimsResponse getClaims(ClaimsRequestDto requestDto) {
        List<ClaimDto> claims = claimsDao.getClaims(requestDto);
        return new ClaimsResponse(claims);
    }

    @Override
    public ClaimsDetailResponse getClaimsDetail(String claimsNumber) {
        List<ClaimDto> claimsDetail = claimsDao.getClaimsDetail(claimsNumber);
        return new ClaimsDetailResponse(claimsDetail);
    }

    @Override
    public ClaimsSkuDetailResponse getClaimsSkuDetail(String claimsNumber, String sku) {
        List<ClaimsSkuDetailDto> claimsDetailBySku = claimsDao.getClaimsDetailBySku(claimsNumber, sku);
        return new ClaimsSkuDetailResponse(claimsDetailBySku);
    }

    @Override
    public void createTask(String claimsNumber, String sku) {
        inforClientService.createTask(claimsNumber, sku);
    }

    @Override
    public void sendToHost(String claimsNumber) {
        inforClientService.sendToHost(claimsNumber);
    }


}
