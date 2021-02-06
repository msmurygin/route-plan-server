package ru.ltmanagement.configuration;

public interface ControllerURL {

    String PUBLIC = "/public";
    String PRIVATE = "/private";


    String VERSION_URL = PUBLIC + "/version";
    String ROUTE_PLAN_HEADER_URL = PRIVATE + "/headertable";
    String ROUTE_PLAN_CODE_LOOK_UP_URL = PRIVATE + "/codelkup";
    String ROUTE_PLAN_CODE_ROUTES_URL = PRIVATE + "/routes";
    String ROUTE_PLAN_CODE_STATUS_URL = PRIVATE + "/statuses";
    String ROUTE_PLAN_ORDER_LIST_URL = PRIVATE +  "/orderlist";
    String ROUTE_PLAN_ORDER_DETAIL_URL = PRIVATE +  "/orderdetail";
    String ROUTE_PLAN_ACTIVE_USERS_URL = PRIVATE +  "/activeusers";
    String ROUTE_PLAN_SKU_STOCK_URL = PRIVATE +  "/skustock";
    String ROUTE_PLAN_CONFIG_URL = PRIVATE +  "/config";
    String ROUTE_PLAN_LOCATION_URL = PRIVATE +  "/location";
    String N_SQL_CONFIG_URL = PRIVATE +  "/nsqlconfig";
    String REPLENISHMENT_TASK_URL = PRIVATE + "/replenishmenttask";
    String REPLENISHMENT_PRIORITY_URL = PRIVATE + "/replenishmentpriority";

    String PROBLEM_LIST_URL = PRIVATE + "/problems";
    String ORDERS = "/orders";
    String REPORTS = "/reports";
    String RELEASE_ORDERS_URL = PRIVATE + ORDERS + "/release";
    String ALLOCATE_ORDERS_URL = PRIVATE + ORDERS + "/allocate";
    String CLOSE_ORDERS_URL = PRIVATE + ORDERS + "/close";
    String CLOSE_ONE_ORDER_URL = PRIVATE + ORDERS + "/closeorder";
    String CANCEL_ORDERS_URL = PRIVATE + ORDERS + "/cancel";
    String SHIP_ORDERS_URL = PRIVATE + ORDERS + "/ship";
    String REPORTS_URL = PRIVATE + REPORTS + "/url";
    String SKU_CLAIMS_URL = PRIVATE +"/skuclaims";
    String CLAIMS_URL = PRIVATE +"/claims";
    String CLAIMS_DETAIL_URL = PRIVATE +"/claimsdetail";
    String CLAIMS_DETAIL_BY_SKU_URL = PRIVATE +"/skuclaimsdetail";
    String CLAIMS_CREATE_INV_TASK_URL = PRIVATE +"/createtasks";
    String SEND_TO_HOST_URL = PRIVATE +"/sendtohost";
}
