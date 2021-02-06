package ru.ltmanagement.replenishment.controller.request;

import ru.ltmanagement.replenishment.dto.ReplenishmentPriorityDto;

import java.io.Serializable;
import java.util.List;


public class ReplenishmentPriorityPutRequest implements Serializable {

    public ReplenishmentPriorityPutRequest(){

    }
    public ReplenishmentPriorityPutRequest(List<ReplenishmentPriorityDto> replenishmentUpdate){
        this.replenishmentUpdate = replenishmentUpdate;
    }
    List<ReplenishmentPriorityDto> replenishmentUpdate;


    public List<ReplenishmentPriorityDto> getReplenishmentUpdate() {
        return replenishmentUpdate;
    }

    public void setReplenishmentUpdate(List<ReplenishmentPriorityDto> replenishmentUpdate) {
        this.replenishmentUpdate = replenishmentUpdate;
    }
}
