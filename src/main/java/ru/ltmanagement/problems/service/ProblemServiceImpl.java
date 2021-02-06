package ru.ltmanagement.problems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.problems.controller.request.ProblemListRequest;
import ru.ltmanagement.problems.dao.ProblemDao;
import ru.ltmanagement.problems.dto.ProblemDto;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    ProblemDao problemDao;

    @Override
    public List<ProblemDto> getProblemList(ProblemListRequest request) {
        return problemDao.getProblems(request);
    }
}
