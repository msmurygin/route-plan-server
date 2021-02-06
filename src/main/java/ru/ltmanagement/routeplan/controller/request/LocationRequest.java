package ru.ltmanagement.routeplan.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {
    private String locationType;
    private String locationCategory;
}
