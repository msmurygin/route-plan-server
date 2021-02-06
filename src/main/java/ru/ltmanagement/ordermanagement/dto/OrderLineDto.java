package ru.ltmanagement.ordermanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineDto {
    private String route;
    private Integer stop;
    private String orderKey;
    private String externalOrderKey2;
    private int orderLines;
    private double pickedQty;
    private double controlledQty;
    private double packedQty;
    private double loadedQty;
    private double calcQtyLane;
    private String packingLocation;
    private String door;
    private double stdCube;
    private double stdGrossWgt;
    private int selectedCartonIdQty;
    private String status;
    private String susr2;
    private int reasonCode;
    private int showReason;
    private String addDate;
    private String routeReady;
    private String actualArrivalDate;
    private String loadStart;
    private String loadEnd;
    private String loadDuration;
    private String shipDate;
    private String vehicleLeftDate;
    private int orderClosed;
    private double leftToControl;
    private double leftToPick;


}
