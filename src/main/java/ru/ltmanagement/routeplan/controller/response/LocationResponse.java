package ru.ltmanagement.routeplan.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LocationResponse {
    private List<String> locations;
}
