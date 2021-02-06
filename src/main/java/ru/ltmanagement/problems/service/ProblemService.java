package ru.ltmanagement.problems.service;

import ru.ltmanagement.problems.controller.request.ProblemListRequest;
import ru.ltmanagement.problems.dto.ProblemDto;

import java.util.List;

public interface ProblemService {
    List<ProblemDto> getProblemList(ProblemListRequest request);
}
