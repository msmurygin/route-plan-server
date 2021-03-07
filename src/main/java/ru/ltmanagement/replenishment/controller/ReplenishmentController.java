package ru.ltmanagement.replenishment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.configuration.ControllerURL;
import ru.ltmanagement.replenishment.controller.request.ReplenishmentPriorityPutRequest;
import ru.ltmanagement.replenishment.controller.request.ReplenishmentPriorityRequest;
import ru.ltmanagement.replenishment.controller.request.ReplenishmentTaskRequest;
import ru.ltmanagement.replenishment.controller.response.ReplenishmentPriorityResponse;
import ru.ltmanagement.replenishment.controller.response.ReplenishmentTaskResponse;
import ru.ltmanagement.replenishment.dto.ReplenishmentPriorityDto;
import ru.ltmanagement.replenishment.dto.ReplenishmentTaskRequestDto;
import ru.ltmanagement.replenishment.service.ReplenishmentService;
import ru.ltmanagement.replenishment.transformer.ReplenishmentTaskTransformer;

import java.util.List;

@RestController
@CrossOrigin( origins = {"http://localhost:4200", "http://sekbtt-wmsapp01.myway.local:4200/" ,"http://sekbtt-wmsapp01.myway.local/"})
public class ReplenishmentController {

    @Autowired
    ReplenishmentService replenishmentService;
    @Autowired
    ReplenishmentTaskTransformer transformer;

    @PostMapping(path = ControllerURL.REPLENISHMENT_TASK_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReplenishmentTaskResponse replenishmentTask(@RequestBody  ReplenishmentTaskRequest req){
        ReplenishmentTaskRequestDto requestDto = transformer.fromReplenishmentTaskRequestToDto(req);
        return new ReplenishmentTaskResponse(replenishmentService.getReplenishmentTask(requestDto));
    }

    @PostMapping(path = ControllerURL.REPLENISHMENT_PRIORITY_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReplenishmentPriorityResponse replenishmentPriority(@RequestBody ReplenishmentPriorityRequest req){
        return new ReplenishmentPriorityResponse(replenishmentService.getReplenishmentPriority(req.getSku(), req.getLoc(), req.getZone()));
    }
    @PutMapping(path = ControllerURL.REPLENISHMENT_PRIORITY_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity replenishmentPriorityUpdate(@RequestBody ReplenishmentPriorityPutRequest req){
        List<ReplenishmentPriorityDto> replenishmentUpdate = req.getReplenishmentUpdate();
        replenishmentService.updateReplenishmentPriority(replenishmentUpdate);
        return ResponseEntity.ok().build();
    }
}
