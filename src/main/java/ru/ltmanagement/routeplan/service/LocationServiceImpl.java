package ru.ltmanagement.routeplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.routeplan.dao.LocationDao;
import ru.ltmanagement.routeplan.service.api.LocationService;

import java.util.List;

@Service
public class LocationServiceImpl  implements LocationService {

    @Autowired
    private LocationDao locationDao;

    @Override
    public List<String> getLocations(String locationType, String locationCategory) {
        return locationDao.getLocations(locationType, locationCategory);
    }
}
