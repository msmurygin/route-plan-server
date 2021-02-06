package ru.ltmanagement.ordermanagement.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderManagementResponse {
    private String header;
    private String message;
}
