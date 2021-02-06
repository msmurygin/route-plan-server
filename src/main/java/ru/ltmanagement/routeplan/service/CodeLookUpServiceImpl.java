package ru.ltmanagement.routeplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ltmanagement.routeplan.dao.CodeLookUpDao;
import ru.ltmanagement.routeplan.dto.CodeLookUpDto;
import ru.ltmanagement.routeplan.service.api.CodeLookUpService;

import java.util.List;

@Service
public class CodeLookUpServiceImpl implements CodeLookUpService {
    @Autowired
    private CodeLookUpDao codeLookUpDao;

    @Override
    public List<CodeLookUpDto> getCodeLookUp(String listName) {
        return codeLookUpDao.getCodeLookUp(listName);
    }

    public List<String> getOrderRoutes(){
        return codeLookUpDao.getRoutes();
    }

    @Override
    public List<CodeLookUpDto> getOrderStatus() {
        return codeLookUpDao.gerOrderStatus();
    }
}
