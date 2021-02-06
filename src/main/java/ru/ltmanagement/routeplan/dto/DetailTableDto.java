package ru.ltmanagement.routeplan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailTableDto {
    private String rowId;//порядковй номер
    private String externalloadid;// ID рейс
    private String deliveryDate;//планируемая дата отгрузки
    private String loadUsr2;// рейс (название рейса)
    private String route;//направление
    private String replenishmentTask;//требует ли рейс пополнения, если требует то = "Детали", иначе = "Нет"
    private int reasonCode;//если у рейса есть причина не резервирования какой либо позиции то = коду причины
    private int showReason;//показывать причину не резервирование если = 1 и есть код причины
    private BigDecimal stdCube;// объем
    private BigDecimal stdGrossWgt;//вес
    private int picked;// % собранного
    private int controlled;// % проконтролированного
    private int packed; // % упакованного
    private int loaded; // % загруженного
    private String status; // статус рейса (в текстовом видет, т.к. название статусов у рейса различаются со статусами заказаов)
    private int itemsInRoute; //кол-во позиций в рейсе всего
    private String packingLocation;//ЕЗУК
    private String door;//ДОК
    private String susr2;//Кладовщик экспедиции
    private String driverName;//Водитель
    private String loadUsr1;//Экспедитор

    //дата и время
    private String addDate;//Создание рейса
    private String routeReady;//Готовность рейса
    private String actualArrivalDate;//Прибытие ТС
    private String loadStart;//Начало погрузки
    private String loadEnd;//Окончание загрузки
    private String loadDuration;//Продолжительность (загрузки)
    private String shipped;//Отгрузка
    private String truckLeaving;//Убытие ТС

    private int shippedItems;//Кол-во отгруженных мест
    private String shift;//смена, если 1 то первая, если 2 то вторая
    private int routeClosed;//закрыт ли рейс, если =1 то уже закрыть и повторно закрыть не получиться
    private int leftToPick;//Док-к (позиций) осталось собрать
    private int leftToControl;//Док-к (позиций) осталось контроль

}
