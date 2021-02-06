package ru.ltmanagement.claims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.claims.controller.request.ClaimsDetailRequest;
import ru.ltmanagement.claims.controller.request.ClaimsRequest;
import ru.ltmanagement.claims.controller.request.ClaimsSkuDetailRequest;
import ru.ltmanagement.claims.controller.response.ClaimsDetailResponse;
import ru.ltmanagement.claims.controller.response.ClaimsResponse;
import ru.ltmanagement.claims.controller.response.ClaimsSkuDetailResponse;
import ru.ltmanagement.claims.controller.response.SkuClaimsResponse;
import ru.ltmanagement.claims.dto.ClaimsRequestDto;
import ru.ltmanagement.claims.service.ClaimsService;
import ru.ltmanagement.claims.trasnformer.ClaimsRequestTransformerProvider;

import java.util.List;
import java.util.stream.Collectors;

import static ru.ltmanagement.configuration.ControllerURL.CLAIMS_CREATE_INV_TASK_URL;
import static ru.ltmanagement.configuration.ControllerURL.CLAIMS_DETAIL_BY_SKU_URL;
import static ru.ltmanagement.configuration.ControllerURL.CLAIMS_DETAIL_URL;
import static ru.ltmanagement.configuration.ControllerURL.CLAIMS_URL;
import static ru.ltmanagement.configuration.ControllerURL.SEND_TO_HOST_URL;
import static ru.ltmanagement.configuration.ControllerURL.SKU_CLAIMS_URL;

@RestController
public class ClaimsController {
    @Autowired
    private ClaimsService claimsService;

    @Autowired
    private ClaimsRequestTransformerProvider transformer;


    @GetMapping(value = SKU_CLAIMS_URL)
    public List<SkuClaimsResponse> skus(){
        return claimsService.getSkus().stream()
                .map(item-> new SkuClaimsResponse(item))
                .collect(Collectors.toList());
    }

    @PostMapping(path = CLAIMS_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClaimsResponse> getClaims(@RequestBody ClaimsRequest request){
        ClaimsRequestDto claimsRequestDto = transformer.transformToDto(request);
        ClaimsResponse claimsResponse = claimsService.getClaims(claimsRequestDto);
        return ResponseEntity.ok(claimsResponse);
    }

    @PostMapping(path = CLAIMS_DETAIL_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClaimsDetailResponse> getClaimsDetails(@RequestBody ClaimsDetailRequest request){
        Assert.notNull(request.getClaimsNumber(), "Please provide claims number");
        String claimsNumber = request.getClaimsNumber();
        ClaimsDetailResponse claimsDetail = claimsService.getClaimsDetail(claimsNumber);
        return ResponseEntity.ok(claimsDetail);
    }

    @PostMapping(path = CLAIMS_DETAIL_BY_SKU_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClaimsSkuDetailResponse> getClaimsDetailsBySKu(@RequestBody ClaimsSkuDetailRequest request){
        Assert.notNull(request.getClaimsNumber(), "Please provide claims number");
        String claimsNumber = request.getClaimsNumber();
        String sku = request.getSku();
        ClaimsSkuDetailResponse claimsSkuDetail = claimsService.getClaimsSkuDetail(claimsNumber, sku);
        return ResponseEntity.ok(claimsSkuDetail);
    }


    @PostMapping(path = CLAIMS_CREATE_INV_TASK_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTasks(@RequestBody ClaimsSkuDetailRequest request){
        Assert.notNull(request.getClaimsNumber(), "Please provide claims number");
        String claimsNumber = request.getClaimsNumber();
        String sku = request.getSku();
        claimsService.createTask(claimsNumber, sku);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = SEND_TO_HOST_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendToHost(@RequestBody ClaimsSkuDetailRequest request){
        Assert.notNull(request.getClaimsNumber(), "Please provide claims number");
        String claimsNumber = request.getClaimsNumber();
        claimsService.sendToHost(claimsNumber);
        return ResponseEntity.ok().build();
    }
}
