package ru.ltmanagement.claims.trasnformer;

import org.mapstruct.Mapper;
import ru.ltmanagement.claims.controller.request.ClaimsRequest;
import ru.ltmanagement.claims.dto.ClaimsRequestDto;

@Mapper(componentModel = "spring")
public interface ClaimsRequestTransformerProvider {

    ClaimsRequestDto transformToDto(ClaimsRequest claimsRequest);


}
