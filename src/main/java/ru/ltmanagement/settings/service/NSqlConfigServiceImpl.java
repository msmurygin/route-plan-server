package ru.ltmanagement.settings.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.settings.NSqlConfigDto;
import ru.ltmanagement.settings.dao.NSqlConfigDao;

@Service
public class NSqlConfigServiceImpl implements NSqlConfigService {

    @Autowired
    NSqlConfigDao nSqlConfigDao;


    @Override
    public NSqlConfigDto getNSqlValue(String configKey) {
        return nSqlConfigDao.getNSqlConfig(configKey);
    }
}
