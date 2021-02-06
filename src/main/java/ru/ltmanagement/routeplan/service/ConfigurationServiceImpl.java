package ru.ltmanagement.routeplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ltmanagement.routeplan.dao.PlanRouteConfigurationDao;
import ru.ltmanagement.routeplan.dto.PlanRouteConfigurationDto;
import ru.ltmanagement.routeplan.service.api.ConfigurationService;
import ru.ltmanagement.settings.dto.SettingsDto;
import ru.ltmanagement.user.dao.UserDao;
import ru.ltmanagement.user.dto.UserDto;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private PlanRouteConfigurationDao configDao;

    @Autowired
    private UserDao userDao;


    private static final boolean IS_UPDATE = false ;
    private static final int WHAT_UPDATE = 0;
    private static final char PARAM_1 = '0';
    private static final int PARAM_2 = 0;

    @Override
    public PlanRouteConfigurationDto getConfiguration(){
        UserDto user = userDao.getUser();
        PlanRouteConfigurationDto configDTO = configDao
                .getPlanRoutConfiguration(user.getLoginId(), IS_UPDATE, WHAT_UPDATE, PARAM_1, PARAM_2);
        return configDTO;
    }

    @Override
    @Transactional
    public void save(SettingsDto settingsDto) {
        UserDto user = userDao.getUser();
        if (configDao.isRecordExistByUser(user.getLoginId())){
            configDao.updateUserSettings(user.getLoginId(), settingsDto);
        }else{
            PlanRouteConfigurationDto defaults = configDao.getDefaults();
            configDao.insertSettings(user.getLoginId(), defaults);
        }
        configDao.updateShiftsSettings(settingsDto);
    }

}
