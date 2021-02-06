package ru.ltmanagement.claims.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.claims.dto.ClaimDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ClaimsResponse {
    private List<ClaimDto> claims;
}
