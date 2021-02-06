package ru.ltmanagement.routeplan.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.routeplan.dto.CodeLookUpDto;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class CodeLookUpResponse {
 private List<CodeLookUpDto> codeLookUps;
}
