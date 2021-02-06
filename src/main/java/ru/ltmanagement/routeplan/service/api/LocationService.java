package ru.ltmanagement.routeplan.service.api;

import java.util.List;

public interface LocationService {
    List<String> getLocations(String locationType, String locationCategory);
}
