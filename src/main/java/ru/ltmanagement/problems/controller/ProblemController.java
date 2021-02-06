package ru.ltmanagement.problems.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.configuration.ControllerURL;
import ru.ltmanagement.problems.controller.request.ProblemListRequest;
import ru.ltmanagement.problems.controller.response.ProblemListResponse;
import ru.ltmanagement.problems.service.ProblemService;

@RestController
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @PostMapping(path = ControllerURL.PROBLEM_LIST_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProblemListResponse problemList(@RequestBody ProblemListRequest request){
        return new ProblemListResponse(problemService.getProblemList(request));
    }
}
