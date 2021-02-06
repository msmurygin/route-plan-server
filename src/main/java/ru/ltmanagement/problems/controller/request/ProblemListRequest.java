package ru.ltmanagement.problems.controller.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemListRequest {
    private String externalLoadId;
    private String orderKey;
}
