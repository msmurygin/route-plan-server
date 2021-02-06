package ru.ltmanagement.replenishment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ltmanagement.replenishment.dao.ReplenishmentTaskDao;
import ru.ltmanagement.replenishment.dto.ReplenishmentPriorityDto;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskDto;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskRequestDto;
import ru.ltmanagement.user.dao.UserDao;
import ru.ltmanagement.user.dto.UserDto;

import java.util.List;

@Service
public class ReplenishmentServiceImpl implements ReplenishmentService {

    @Autowired
    ReplenishmentTaskDao taskDao;

    @Autowired
    UserDao userDAOImpl;

    @Override
    public List<ReplenishmentTaskDto> getReplenishmentTask(ReplenishmentTaskRequestDto requestDto) {
        UserDto user = userDAOImpl.getUser();
        return  taskDao.getReplenishmentTask(user.getLoginId(), requestDto);
    }

    @Override
    public List<ReplenishmentPriorityDto> getReplenishmentPriority(String sku, String loc, String zone) {
        return taskDao.getReplenishmentPriority(sku, loc, zone);
    }

    @Override
    @Transactional
    public void updateReplenishmentPriority(List<ReplenishmentPriorityDto> replenishmentUpdate) {
        taskDao.updateReplenishmentPriority(replenishmentUpdate);
    }
}
