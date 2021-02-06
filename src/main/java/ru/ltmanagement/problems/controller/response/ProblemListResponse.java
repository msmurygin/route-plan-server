package ru.ltmanagement.problems.controller.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ltmanagement.problems.dto.ProblemDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProblemListResponse {
    private List<ProblemDto> problems;
}
